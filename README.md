## Smart Street Lighting Microservices Concept
(warn : Proposal only)

### General
Nowadays, it's a great problem with light pollution in cities. 
Smart street lights could improve the situation by decreasing the intensity of the emitted light. 
The lights in cities are usually inefficient and are still turned on with the maximum intensity. 
It could also save a lot of resources provided by these cities and especially save environment, which is affected by that. 

These street lamps would contain some basic set I/O devices, because we need to sense the surroundings of that. 
We need to detect people, measure ambient light and change intensity of these lights (PIR sensor, photoresistor, PWM). 
Basic specification of the street lamp:
<ul>
<li> transmit data to the server, where are processing, and some response is sent back.</li>
<li> has neighbours, which are affected by the lamp, so there should be defined some association among the adjacent lamps. (the neighbours will be turned on too with lower intensity)</li>
<li> define time quantum after the lamps are turned off, if there's no traffic.</li>
<li> lamps on more frequent places could have greater time quantum</li>
<li> (optional) maybe somehow measure speed of people (trigger objects) and react to that. </li>
</ul>

### Services
It's necessary to create some hierarchy of these street lamps, in order to locate them. 
It should be possible to get lights from specific cities, streets and get particular lights. 
Lately, it should be possible to add external sensors, so the lamps could be some kind of device composed of another devices, so I'd prefer mark that as devices too. 

I'd suggest deploy these services:
<ul>
<li>User service - admins, maintainers,...</li>
<li>Auth service - AuthN and AuthZ</li>
<li>City service</li>
<li>Street service</li>
<li>Device service - core functionality</li>
<li>(optional) Data Simulation service - create simulated data from sensors</li>
</ul>

### Implementation
Here's a proposal for tools/services used for implementing the components of the system.
<ul>
<li>Server - Quarkus</li>
<li>ORM - Hibernate with Panache</li>
<li>DB - arbitrary (default H2)</li>
<li>REST - Mutiny (async)</li>
<li>Metrics - Prometheus</li>
<li>API visualize - Swagger UI</li>
</ul>

For bulk operations, there should be used pagination. f.e. `getAll(int firstResult, int maxResults)`, 
which takes the `maxResults` items from `firstResult` index.

#### Basic Structure of microservices
<ul>
<li>/entity - DTO</li>
<li>/resources - REST</li>
<li>/services - Business logic</li>
<li>/health - Health check</li>
<li>...</li>
</ul>





