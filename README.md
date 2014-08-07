SnapHome
======

Legrand/BTicino SCS BUS My Home Android management solution 

The goal of the project is to provide a full solution to provide domotic management.
The solution is based on different components:

- engine project: the base library providing components
driving your device. Today some are manage as Light, Heating,
Gateway, ... The project is totally compliant with some of JAVA
stack (OSGi, Android, other java platform).
- android project: an android front end using engine.
- server project: a gateway simulator to test your application. Based
on OSGi, it is enough modulable to add new device easily. Some device
are provided as sub project.
- web project: a web front end based on engine, OSGi and DOJO framework.

Even if today only SCS protocole (by the way of open web net) is supported, in the backlog,
there is the wish to support in the engine different protocle (KNX, XPL, ...)... 
