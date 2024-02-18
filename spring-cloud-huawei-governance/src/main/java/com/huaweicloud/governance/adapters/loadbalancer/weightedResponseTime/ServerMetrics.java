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

package com.huaweicloud.governance.adapters.loadbalancer.weightedResponseTime;

import java.util.concurrent.TimeUnit;

import io.github.resilience4j.core.metrics.FixedSizeSlidingWindowMetrics;
import io.github.resilience4j.core.metrics.Metrics.Outcome;
import io.github.resilience4j.core.metrics.Snapshot;

/**
 * ServiceCombServer states
 */
public class ServerMetrics {
  private final FixedSizeSlidingWindowMetrics metrics = new FixedSizeSlidingWindowMetrics(50);

  public void record(long duration, TimeUnit durationUnit, Outcome outcome) {
    metrics.record(duration, durationUnit, outcome);
  }

  public Snapshot getSnapshot() {
    return metrics.getSnapshot();
  }
}
