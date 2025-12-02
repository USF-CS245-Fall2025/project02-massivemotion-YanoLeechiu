MassiveMotion draws an orange sun at the center of the screen and spawns white stars
from the window edges that move across the screen. You can change the list implementations
by inputting in the txt file that you choose to input, given that it ahs the same parameters.

How to use:
1. Change the MassiveMotion.txt file to the desired parameters and List Type you want to implement:
   - arraylist
   - dummyheadlinkedlist 
   - doublylinkedlist
   - linkedlist
2. Compile with javac MassiveMotion.java
3. java MassiveMotion MassiveMotion.txt


Quick Description: 
ArrayList - growth is by doubling; random access is constant, but shifting makes middle inserts/removes linear.

LinkedList - (singly, head only) has no tail pointer, so appends require a full walk.

DummyHeadLinkedList - uses a sentinel to make head operations cleaner. Very similar to LinkedList.

DoublyLinkedList - can walk from the nearer end using nodeAt(); tail pointer makes append/pop-back O(1).

Quick Runtimes: 
Structure	        get(i)	add(e)	    addAtIndex(i,e) 	removeAtIndex(i)
ArrayList	        O(1)	O(1)	    O(n)(shift)	         O(n) (shift)
LinkedList  	    O(n)	O(n)	    O(n)	             O(n)
DHLinkedList	    O(n)	O(n)	    O(n)	             O(n)
DoublyLinkedList	O(n/2)	O(1)	    O(n/2)	             O(n/2)
 

Class Descriptions: 

CelestialBody: 
- x,y = position 
- vx,yv = velocity 
- contains radius, color, and isStar
- Step() - dictates each tick where x and y are updated depending on velocity
- isOffScreen() - true if the circle is fully out of bounds, and gets removed from animation
- draw() - draws a filled circle at x,y per star

MassiveMotion: 
- Loads configuration from the txt file, sets up window and creates timer. 
- Creates the sun body and adds it to bodies
- on each timer tick: Move all bodies → Cull offscreen = true → Maybe spawn edge stars → repaint().

List: 
- int size()
- boolean add(T element) - append
- void add(int index, T element) - add at index
- T get(int index) - read
- T remove(int index) - remove at index


Zoom demonstration link: https://us06web.zoom.us/clips/share/lrU51IwITkabQgA77INiHw