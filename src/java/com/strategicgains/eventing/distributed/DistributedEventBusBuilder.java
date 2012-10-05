/*
    Copyright 2012, Strategic Gains, Inc.

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
package com.strategicgains.eventing.distributed;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.config.Config;
import com.strategicgains.eventing.EventBusBuilder;
import com.strategicgains.eventing.EventHandler;

/**
 * @author toddf
 * @since Oct 5, 2012
 */
public class DistributedEventBusBuilder
implements EventBusBuilder<DistributedEventBus, DistributedEventBusBuilder>
{
	private static final String DEFAULT_QUEUE_NAME = "domain-events";

	private Config config = null;
	private String queueName = DEFAULT_QUEUE_NAME;
	private List<EventHandler> subscribers = new ArrayList<EventHandler>();

	public DistributedEventBusBuilder()
	{
		super();
	}

    @Override
    public DistributedEventBusBuilder subscribe(EventHandler handler)
    {
    	if (!subscribers.contains(handler))
    	{
    		subscribers.add(handler);
    	}
    	
    	return this;
    }

	@Override
	public DistributedEventBus build()
	{
		return new DistributedEventBus(queueName, config, subscribers);
	}
}
