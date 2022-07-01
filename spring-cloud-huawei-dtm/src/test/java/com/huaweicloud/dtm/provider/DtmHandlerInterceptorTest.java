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

package com.huaweicloud.dtm.provider;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import com.huaweicloud.dtm.DtmContextDTO;
import com.huaweicloud.dtm.util.DtmConstants;

import com.huawei.middleware.dtm.client.context.DTMContext;

import io.vertx.core.json.Json;
import mockit.Expectations;
import mockit.Injectable;

/**
 * @Author wangqijun
 * @Date 10:45 2019-09-29
 **/
public class DtmHandlerInterceptorTest {

  @Test
  public void preHandle(@Injectable HttpServletRequest request) throws Exception {
    DtmHandlerInterceptor dtmHandlerInterceptor = new DtmHandlerInterceptor();
    DtmContextDTO dtmContextDTO = new DtmContextDTO();
    dtmContextDTO.setGlobalTxId(100);
    new Expectations() {
      {
        request.getHeader(DtmConstants.DTM_CONTEXT);
        result = Json.encode(dtmContextDTO);
      }
    };
    dtmHandlerInterceptor.preHandle(request, null, null);

    assertEquals(DTMContext.getDTMContext().getGlobalTxId(), 100);
  }
}