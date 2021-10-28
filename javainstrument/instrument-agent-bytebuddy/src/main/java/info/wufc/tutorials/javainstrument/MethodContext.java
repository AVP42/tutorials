/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package info.wufc.tutorials.javainstrument;

public class MethodContext {
    private boolean isContinue = true;

    private Object ret = null;

    private Object context;

    public void defineReturnValue(Object ret) {
        this.isContinue = false;
        this.ret = ret;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public Object _ret() {
        return ret;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "MethodContext{" +
                "isContinue=" + isContinue +
                ", ret=" + ret +
                ", context=" + context +
                '}';
    }
}
