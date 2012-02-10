Overview
========
Domain-Eventing implements the Domain Events concept from Eric Evans' Domain Driven Design.

This simple Java library provides a Singleton interface (see DomainEvents class) to an EventMonitor thread and a simple
way to raise events (which are just POJOs) throughout the domain layer.

Event handlers simply implement the EventHandler interface, which has two methods, handle(Object) and handles(Class).
The handle() method is the implementation for dealing with the domain event and handles() returns a boolean indicating
whether that particular EventHandler can process the given class.

Event Flow
==========
### Domain Model
<table border="0">
	<tr>
		<td>DomainEvents.raise(event)</td>
		<td>--------&gt;</td>
		<td>eventMonitor.raise(event)</td>
		<td>--------&gt;</td>
		<td>concurrentQueue.add(event)<br/>monitorThread.notify()</td>
	</tr>
</table>
### Monitor Thread (on notify)
<table border="0">
	<tr>
		<td>event = concurrentQueue.poll()</td>
		<td>--------&gt;</td>
		<td>Get handlers that can process the given event.<br/>Utilizes handler.handles(event).<br/>This collection of handlers<br/>is cached for later use.</td>
		<td>--------&gt;</td>
		<td>handler.handle(event)<br/>(for each handler)</td>
	</tr>
</table>

Usage
=====
1. Implement *EventHandler* interface in class(es) to process appropriate events.
   1. Implement *handles(Class)* method to return true for each DomainEvent type that the handler can process.
   2. Implement *handle(Object)* to actually process the event.
2. Call *DomainEvents.register(EventHandler)* for each EventHandler implementation.
3. Call *DomainEvents.startMonitoring()* at the beginning of your application.
4. Call *DomainEvents.raise(Object)* in your domain code where events need to be raised.
   - repeat as necessary.
5. Call *DomainEvents.stopMonitoring()* at the end of your application.

Want more than a single thread and queue to handle your domain events?  Cool!  Then ignore the static foreign
methods in the *DomainEvents* class and utilize the *EventMonitor* thread alone.  Create as many instances of 
*EventMonitor* as you need.  Aside from the creation of *DomainEvent* and *EventHandler* implementations
(those parts are the same, see steps #1 & #2 above), here's the way to use *EventMonitor* on its own:

1. monitor = new EventMonitor()
2. monitor.register(EventHandler) for each EventHandler implementation.
3. monitor.start()
4. monitor.raise(DomainEvent) in your domain logic.
   - repeat as necessary
5. monitor.shutdown()

Release Notes
=============
### 0.2.0 - under development
* Removed constraint of having to implement DomainEvent marker interface in event messages.
* Introduced EventQueue, allowing multiple EventMonitor threads to be processing events from the queue simultaneously.

### 0.1.0
Initial release.
