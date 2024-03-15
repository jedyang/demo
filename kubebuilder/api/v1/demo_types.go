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
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
)

// EDIT THIS FILE!  THIS IS SCAFFOLDING FOR YOU TO OWN!
// NOTE: json tags are required.  Any new fields you add must have json tags for the fields to be serialized.

// DemoSpec defines the desired state of Demo
// 期望的状态
type DemoSpec struct {
	// INSERT ADDITIONAL SPEC FIELDS - desired state of cluster
	// Important: Run "make" to regenerate code after modifying this file

	//注意: json 标签是必需的。为了能够序列化字段，任何你添加的新的字段一定有json标签。

	// 使用+指定额外的元数据，比如kubebuilder:validation:Required，这个指定了这个字段是必需的。
	// 在生成 CRD 清单时，controller-tools 将使用这些数据来生成相应的验证规则。

	// +kubebuilder:validation:Required
	Image string `json:"image,omitempty"`
	// +kubebuilder:validation:Required
	SvcName string `json:"svcName,omitempty"`
	// +kubebuilder:validation:Required
	Replicas *int32 `json:"replicas,omitempty"`
}

// DemoStatus defines the observed state of Demo
// 观察到的状态，供用户或者其他controller使用
type DemoStatus struct {
	// INSERT ADDITIONAL STATUS FIELD - define observed state of cluster
	// Important: Run "make" to regenerate code after modifying this file

	Replicas *int32 `json:"replicas,omitempty"`
}

// 这个标记告诉 controller-tools 生成的代码，这个类型是一个Kind
//+kubebuilder:object:root=true
//+kubebuilder:subresource:status

// Demo is the Schema for the demoes API
type Demo struct {
	metav1.TypeMeta   `json:",inline"`
	metav1.ObjectMeta `json:"metadata,omitempty"`

	Spec   DemoSpec   `json:"spec,omitempty"`
	Status DemoStatus `json:"status,omitempty"`
}

//+kubebuilder:object:root=true

// DemoList contains a list of Demo
type DemoList struct {
	metav1.TypeMeta `json:",inline"`
	metav1.ListMeta `json:"metadata,omitempty"`
	Items           []Demo `json:"items"`
}

func init() {
	SchemeBuilder.Register(&Demo{}, &DemoList{})
}
