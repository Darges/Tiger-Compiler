#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// "new" a new object, do necessary initializations, and
// return the pointer (reference).
void *Tiger_new (void *vtable, int size)
{
  // You should write 4 statements for this function.
  // #1: "malloc" a chunk of memory of size "size":
  void* tempPointer = malloc(size);  //首先为对应类分配空间
  //void* tempVtable  = malloc(sizeof(*vtable));
  // #2: clear this chunk of memory (zero off it):
  memset(tempPointer, 0, size);      //把这个类对应的内存空间清空

  // #3: set up the "vtable" pointer properly:
  //memcpy(tempVtable , vtable, sizeof(*vtable));
  memcpy(tempPointer, &vtable, sizeof(void *));//在对应空间的头部存入虚函数表的地址
  //memcpy(tempPointer, vtable, sizeof(*vtable));
  // #4: return the pointer
  return tempPointer;
}
