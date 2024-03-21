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

package v1

import (
	apierrors "k8s.io/apimachinery/pkg/api/errors"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"k8s.io/apimachinery/pkg/util/validation/field"
	ctrl "sigs.k8s.io/controller-runtime"
	logf "sigs.k8s.io/controller-runtime/pkg/log"
	"sigs.k8s.io/controller-runtime/pkg/webhook"
)

// log is for logging in this package.
var demolog = logf.Log.WithName("demo-resource")

func (r *Demo) SetupWebhookWithManager(mgr ctrl.Manager) error {
	return ctrl.NewWebhookManagedBy(mgr).
		For(r).
		Complete()
}

// TODO(user): EDIT THIS FILE!  THIS IS SCAFFOLDING FOR YOU TO OWN!

//+kubebuilder:webhook:path=/mutate-tutorial-demo-com-v1-demo,mutating=true,failurePolicy=fail,sideEffects=None,groups=tutorial.demo.com,resources=demoes,verbs=create;update,versions=v1,name=mdemo.kb.io,admissionReviewVersions=v1

var _ webhook.Defaulter = &Demo{}

// Default implements webhook.Defaulter so a webhook will be registered for the type
func (r *Demo) Default() {
	demolog.Info("default", "name", r.Name)

	// TODO(user): fill in your defaulting logic.
	if r.Spec.Replicas == nil {
		r.Spec.Replicas = new(int32)
		*r.Spec.Replicas = 1
		demolog.Info("配置默认值", "replicas", *r.Spec.Replicas)
	}
}

// 注意这行提示，如果要删除时也进行验证，需要加verbs
// TODO(user): change verbs to "verbs=create;update;delete" if you want to enable deletion validation.
//+kubebuilder:webhook:path=/validate-tutorial-demo-com-v1-demo,mutating=false,failurePolicy=fail,sideEffects=None,groups=tutorial.demo.com,resources=demoes,verbs=create;update,versions=v1,name=vdemo.kb.io,admissionReviewVersions=v1

var _ webhook.Validator = &Demo{}

// ValidateCreate implements webhook.Validator so a webhook will be registered for the type
func (r *Demo) ValidateCreate() error {
	demolog.Info("validate create", "name", r.Name)

	// TODO(user): fill in your validation logic upon object creation.
	// 调用 r.validate() 方法，来验证对象的合法性。
	return r.validate()
}

// ValidateUpdate implements webhook.Validator so a webhook will be registered for the type
func (r *Demo) ValidateUpdate(old runtime.Object) error {
	demolog.Info("validate update", "name", r.Name)

	// TODO(user): fill in your validation logic upon object update.
	// 调用 r.validate() 方法，来验证对象的合法性。
	return r.validate()
}

// ValidateDelete implements webhook.Validator so a webhook will be registered for the type
func (r *Demo) ValidateDelete() error {
	demolog.Info("validate delete", "name", r.Name)

	// TODO(user): fill in your validation logic upon object deletion.
	// 删除时就不调用了
	return nil
}

func (r *Demo) validate() error {
	var allErrs field.ErrorList
	if *r.Spec.Replicas > 10 {
		err := field.Invalid(field.NewPath("spec").Child("replicas"),
			*r.Spec.Replicas,
			"副本数不能大于10")

		allErrs = append(allErrs, err)
	}

	if len(allErrs) == 0 {
		demolog.Info("参数合法")
		return nil
	}

	return apierrors.NewInvalid(schema.GroupKind{
		Group: "tutorial",
		Kind:  "Demo"},
		r.Name, allErrs)
}
