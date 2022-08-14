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
 */
package org.apache.rocketmq.tools.command.acl;

import org.apache.commons.cli.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.AclConfig;
import org.apache.rocketmq.common.PlainAccessConfig;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.srvutil.ServerUtil;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.command.SubCommandException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

@RunWith(MockitoJUnitRunner.class)
public class GetAccessConfigSubCommandTest {
    @Mock
    private DefaultMQAdminExt mqAdminExt;
    @Mock
    private AclConfig aclConfig;

    @Before
    public void init() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        List<String> globalWhiteAddrs = new ArrayList<>();
        List<PlainAccessConfig> configs = new ArrayList<>();
        when(aclConfig.getPlainAccessConfigs()).thenReturn(configs);
        when(aclConfig.getGlobalWhiteAddrs()).thenReturn(globalWhiteAddrs);
        when(mqAdminExt.examineBrokerClusterAclConfig(anyString())).thenReturn(aclConfig);
    }

    @Test
    public void testExecute() throws SubCommandException {
        GetAccessConfigSubCommand cmd = new GetAccessConfigSubCommand();
        Options options = ServerUtil.buildCommandlineOptions(new Options());
        String[] subargs = new String[] {"-c default-cluster", "-b 127.0.0.1:10911"};
        final CommandLine commandLine =
                ServerUtil.parseCmdLine("mqadmin " + cmd.commandName(), subargs, cmd.buildCommandlineOptions(options), new PosixParser());
        assertThat(commandLine.getOptionValue('c').trim()).isEqualTo("default-cluster");
        assertThat(commandLine.getOptionValue('b').trim()).isEqualTo("127.0.0.1:10911");
        cmd.execute(commandLine, options, null);
    }


}
