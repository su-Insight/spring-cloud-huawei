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

package com.huaweicloud.governance.authentication;

import org.springframework.core.env.Environment;

import com.huaweicloud.governance.GovernanceConst;

public class MatcherUtils {
  public static boolean isPatternMatch(String value, String pattern) {
    if (pattern.startsWith("*") || pattern.startsWith("/*")) {
      int index = 0;
      for (int i = 0; i < pattern.length(); i++) {
        if (pattern.charAt(i) != '*' && pattern.charAt(i) != '/') {
          break;
        }
        index++;
      }
      return value.endsWith(pattern.substring(index));
    }
    if (pattern.endsWith("*")) {
      return value.startsWith(pattern.substring(0, pattern.length() - 1));
    }
    return value.equals(pattern);
  }

  public static boolean isMatchUriWhitelist(String uri, Environment environment) {
    String whiteList = environment.getProperty(GovernanceConst.AUTH_API_PATH_WHITELIST, String.class, "");
    if (whiteList.isEmpty()) {
      return false;
    }
    for (String whiteUri : whiteList.split(",")) {
      if (!whiteUri.isEmpty() && MatcherUtils.isPatternMatch(uri, whiteUri)) {
        return true;
      }
    }
    return false;
  }
}
