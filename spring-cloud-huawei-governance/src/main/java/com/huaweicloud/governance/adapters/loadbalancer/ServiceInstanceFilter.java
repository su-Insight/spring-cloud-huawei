/*

  * Copyright (C) 2020-2022 Huawei Technologies Co., Ltd. All rights reserved.

  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.huaweicloud.governance.adapters.loadbalancer;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.core.Ordered;

public interface ServiceInstanceFilter extends Ordered {
    int ZONE_AWARE_ORDER = -2;

    int CANARY_ORDER = -1;

    int AFFINITY_TAG_ORDER = 0;

    /**
     * isolation order master before zoneAware
     * ensure that other az instances can be called after same az instances isolation.
     */
    int INSTANCE_ISOLATION_ORDER = -3;

    /**
     * filter service instance
     *
     * @param supplier invoker of the filter
     * @param instances service instances
     * @param request request
     * @return filtered instance list
     */
    List<ServiceInstance> filter(ServiceInstanceListSupplier supplier, List<ServiceInstance> instances, Request<?> request);
}
