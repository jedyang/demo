/*
Copyright 2024.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controllers

import (
	"context"
	tutorialv1 "demo.com/tutorial/api/v1"
	"k8s.io/apimachinery/pkg/api/resource"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/types"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"
	"sigs.k8s.io/controller-runtime/pkg/controller/controllerutil"
	"sigs.k8s.io/controller-runtime/pkg/log"

	appsv1 "k8s.io/api/apps/v1"
	corev1 "k8s.io/api/core/v1"
	apierrors "k8s.io/apimachinery/pkg/api/errors"
)

// DemoReconciler reconciles a Demo object
type DemoReconciler struct {
	client.Client
	Scheme *runtime.Scheme
}

// 正常来说这些都应该定义在spec里
const (
	// 容器port，我准备用ng的镜像，所以用了80
	C_PORT      = 80
	CPU_REQUEST = "100m"
	CPU_LIMIT   = "100m"
	MEM_REQUEST = "512Mi"
	MEM_LIMIT   = "512Mi"
)

// 一组rbac标记，用于生成config/rbac/role.yaml
//+kubebuilder:rbac:groups=tutorial.demo.com,resources=demoes,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=tutorial.demo.com,resources=demoes/status,verbs=get;update;patch
//+kubebuilder:rbac:groups=tutorial.demo.com,resources=demoes/finalizers,verbs=update
//+kubebuilder:rbac:groups=core,resources=services,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=apps,resources=deployments,verbs=get;list;watch;create;update;patch;delete

// Reconcile is part of the main kubernetes reconciliation loop which aims to
// move the current state of the cluster closer to the desired state.
// TODO(user): Modify the Reconcile function to compare the state specified by
// the Demo object against the actual cluster state, and then
// perform operations to make the cluster state reflect the state specified by
// the user.
//
// For more details, check Reconcile and its Result here:
// - https://pkg.go.dev/sigs.k8s.io/controller-runtime@v0.10.0/pkg/reconcile
func (r *DemoReconciler) Reconcile(ctx context.Context, req ctrl.Request) (ctrl.Result, error) {
	myLog := log.FromContext(ctx)
	myLog.Info("===reconciling demo custom resource===")

	var demo tutorialv1.Demo
	myLog.Info("req.NamespacedName-->", "req.NamespacedName", req.NamespacedName)
	// 获取我们的自定义资源
	if err := r.Get(ctx, req.NamespacedName, &demo); err != nil {
		myLog.Error(err, "unable to fetch demo")
		// client.IgnoreNotFound(err) 会忽略没找到的错误，这样外部就不再立即调用Reconcile方法了
		return ctrl.Result{}, client.IgnoreNotFound(err)
	}

	// 如果处于正在删除的状态，直接返回
	if demo.DeletionTimestamp != nil {
		return ctrl.Result{}, nil
	}

	// 检测是否有已创建的关联 Deployment
	var deploy appsv1.Deployment
	key := types.NamespacedName{Namespace: demo.Namespace, Name: demo.Spec.SvcName}

	err := r.Get(ctx, key, &deploy)
	if err != nil && !apierrors.IsNotFound(err) {
		return ctrl.Result{}, err
	}
	if err != nil && apierrors.IsNotFound(err) {
		// 去创建关联的 Deployment

		// 先要创建service
		if err = r.createServiceIfNotExists(ctx, &demo); err != nil {
			myLog.Error(err, "createServiceIfNotExists error")
			// 返回错误信息给外部
			return ctrl.Result{}, err
		}

		// 创建deployment
		if err = r.createDeployment(ctx, &demo); err != nil {
			myLog.Error(err, "createDeployment error")
			// 返回错误信息给外部
			return ctrl.Result{}, err
		}

		// 如果创建成功就更新状态
		if err = r.updateStatus(ctx, &demo); err != nil {
			myLog.Error(err, "updateStatus error")
			// 返回错误信息给外部
			return ctrl.Result{}, err
		}

		// 创建成功就可以返回了
		return ctrl.Result{}, nil
	}

	// 如果查到了deployment，并且没有返回错误，就是更新逻辑

	// 期望的副本数
	expectReplicas := demo.Spec.Replicas

	// 当前deployment的实际副本数
	realReplicas := deploy.Spec.Replicas

	// 如果相等，就直接返回了
	if expectReplicas == realReplicas {
		return ctrl.Result{}, nil
	}
	myLog.Info("调整副本数")
	// 如果不等，就要调整
	deploy.Spec.Replicas = expectReplicas

	// 通过客户端更新deployment
	if err = r.Update(ctx, &deploy); err != nil {
		// 如果遇到更新冲突，重新获取资源并重试
		if apierrors.IsConflict(err) {
			// 重新获取最新的资源版本
			err := r.Get(ctx, key, &deploy)
			if err == nil {
				// 再次尝试应用更改和更新
				deploy.Spec.Replicas = expectReplicas
				err = r.Update(ctx, &deploy)
			}

		}
		// 处理其他类型的错误
		if err != nil {
			myLog.Error(err, "update deployment replicas error")
			// 返回错误信息给外部
			return ctrl.Result{}, err
		}

	}

	// 如果更新deployment的Replicas成功，就更新状态
	if err = r.updateStatus(ctx, &demo); err != nil {
		// 返回错误信息给外部
		return ctrl.Result{}, err
	}

	myLog.Info("demo custom resource reconciled")

	return ctrl.Result{}, nil
}

// 新建service
func (r *DemoReconciler) createServiceIfNotExists(ctx context.Context, instance *tutorialv1.Demo) error {
	myLog := log.FromContext(ctx)

	service := &corev1.Service{}

	key := types.NamespacedName{Namespace: instance.Namespace, Name: instance.Spec.SvcName}

	err := r.Get(ctx, key, service)

	// 如果查询结果没有错误，证明service正常，就不做任何操作
	if err == nil {
		myLog.Info("service exists")
		return nil
	}

	// 如果错误不是NotFound，就返回错误
	if !apierrors.IsNotFound(err) {
		myLog.Error(err, "query service error")
		return err
	}

	// 实例化一个service
	service = &corev1.Service{
		ObjectMeta: metav1.ObjectMeta{
			Namespace: instance.Namespace,
			Name:      instance.Spec.SvcName,
		},
		Spec: corev1.ServiceSpec{
			Ports: []corev1.ServicePort{{
				Name: "http",
				Port: C_PORT,
			},
			},
			Selector: map[string]string{
				"app": instance.Spec.SvcName,
			},
			Type: corev1.ServiceTypeNodePort,
		},
	}

	// 这一步非常关键！
	// 建立关联后，删除自定义资源时就会将关联的service也删除掉
	myLog.Info("set reference")
	if err := controllerutil.SetControllerReference(instance, service, r.Scheme); err != nil {
		myLog.Error(err, "SetControllerReference error")
		return err
	}

	// 创建service
	myLog.Info("start create service")
	if err := r.Create(ctx, service); err != nil {
		myLog.Error(err, "create service error")
		return err
	}

	myLog.Info("create service success")

	return nil
}

// 新建deployment
func (r *DemoReconciler) createDeployment(ctx context.Context, instance *tutorialv1.Demo) error {
	myLog := log.FromContext(ctx)
	// 实例化一个数据结构
	deployment := &appsv1.Deployment{
		ObjectMeta: metav1.ObjectMeta{
			Namespace: instance.Namespace,
			Name:      instance.Spec.SvcName,
		},
		Spec: appsv1.DeploymentSpec{
			Replicas: instance.Spec.Replicas,
			Selector: &metav1.LabelSelector{
				MatchLabels: map[string]string{
					"app": instance.Spec.SvcName,
				},
			},

			Template: corev1.PodTemplateSpec{
				ObjectMeta: metav1.ObjectMeta{
					Labels: map[string]string{
						"app": instance.Spec.SvcName,
					},
				},
				Spec: corev1.PodSpec{
					Containers: []corev1.Container{
						{
							Name: instance.Spec.SvcName,
							// 用指定的镜像
							Image:           instance.Spec.Image,
							ImagePullPolicy: "IfNotPresent",
							Ports: []corev1.ContainerPort{
								{
									Name:          "http",
									Protocol:      corev1.ProtocolTCP,
									ContainerPort: C_PORT,
								},
							},
							Resources: corev1.ResourceRequirements{
								Requests: corev1.ResourceList{
									"cpu":    resource.MustParse(CPU_REQUEST),
									"memory": resource.MustParse(MEM_REQUEST),
								},
								Limits: corev1.ResourceList{
									"cpu":    resource.MustParse(CPU_LIMIT),
									"memory": resource.MustParse(MEM_LIMIT),
								},
							},
						},
					},
				},
			},
		},
	}

	// 这一步非常关键！
	// 建立关联后，删除自定义资源时就会将deployment也删除掉
	myLog.Info("set reference")
	if err := controllerutil.SetControllerReference(instance, deployment, r.Scheme); err != nil {
		myLog.Error(err, "SetControllerReference error")
		return err
	}

	// 创建deployment
	myLog.Info("start create deployment")
	if err := r.Create(ctx, deployment); err != nil {
		myLog.Error(err, "create deployment error")
		return err
	}

	myLog.Info("create deployment success")

	return nil
}

func (r *DemoReconciler) updateStatus(ctx context.Context, instance *tutorialv1.Demo) error {
	myLog := log.FromContext(ctx)

	instance.Status.Replicas = instance.Spec.Replicas

	if err := r.Update(ctx, instance); err != nil {
		myLog.Error(err, "update instance error")
		return err
	}

	return nil
}

// var (
//
//	indexName = ".metadata.name"
//
// )

// SetupWithManager sets up the controller with the Manager.
func (r *DemoReconciler) SetupWithManager(mgr ctrl.Manager) error {
	// 建立pod name的索引
	/*	if err := mgr.GetFieldIndexer().IndexField(
			context.Background(),
			&corev1.Pod{},
			".metadata.name",
			func(rawObj client.Object) []string {
				pod := rawObj.(*corev1.Pod)
				return []string{pod.Name}
			},
		); err != nil {
			return err
		}*/

	return ctrl.NewControllerManagedBy(mgr).
		For(&tutorialv1.Demo{}).
		Complete(r)
}
