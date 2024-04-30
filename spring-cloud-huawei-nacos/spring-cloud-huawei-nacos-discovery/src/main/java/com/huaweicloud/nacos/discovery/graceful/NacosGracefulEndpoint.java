/*

 * Copyright (C) 2020-2024 Huawei Technologies Co., Ltd. All rights reserved.

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

package com.huaweicloud.nacos.discovery.graceful;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.eventbus.EventBus;
import com.huaweicloud.common.configration.dynamic.GovernanceProperties;
import com.huaweicloud.common.event.EventManager;
import com.huaweicloud.nacos.discovery.NacosDiscoveryProperties;
import com.huaweicloud.nacos.discovery.registry.NacosAutoServiceRegistration;
import com.huaweicloud.nacos.discovery.registry.NacosRegistration;
import com.huaweicloud.nacos.discovery.registry.NacosServiceRegistrationEvent;
import com.huaweicloud.nacos.discovery.registry.NacosServiceRegistry;

@Endpoint(id = "nacos-service-registry")
public class NacosGracefulEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(NacosGracefulEndpoint.class);

  private final NacosServiceRegistry nacosServiceRegistry;

  private final NacosRegistration nacosRegistration;

  private final NacosAutoServiceRegistration nacosAutoServiceRegistration;

  private final NacosDiscoveryProperties nacosDiscoveryProperties;

  private final EventBus eventBus;

  public NacosGracefulEndpoint(NacosServiceRegistry nacosServiceRegistry, NacosRegistration nacosRegistration,
      NacosAutoServiceRegistration nacosAutoServiceRegistration, NacosDiscoveryProperties nacosDiscoveryProperties) {
    this.nacosServiceRegistry = nacosServiceRegistry;
    this.nacosRegistration = nacosRegistration;
    this.nacosAutoServiceRegistration = nacosAutoServiceRegistration;
    this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    eventBus = EventManager.getEventBus();
  }

  @WriteOperation
  public void gracefulUpperAndDown(@Nullable String status) {
    if (StringUtils.isEmpty(status)) {
      return;
    }
    if (GovernanceProperties.GRASEFUL_STATUS_UPPER.equalsIgnoreCase(status)) {
      nacosDiscoveryProperties.setRegisterEnabled(true);
      nacosAutoServiceRegistration.registryExtend();
    } else if (GovernanceProperties.GRASEFUL_STATUS_DOWN.equalsIgnoreCase(status)) {
      nacosServiceRegistry.deregister(nacosRegistration);
      eventBus.post(new NacosServiceRegistrationEvent(new Instance(), false));
    } else {
      LOGGER.warn("operation is not allowed, status: " + status);
    }
  }
}
