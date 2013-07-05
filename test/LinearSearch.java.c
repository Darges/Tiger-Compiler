// This is automatically generated by the Tiger compiler.
// Do NOT modify!

//declaration
struct LinearSearch_vtable LinearSearch_vtable_;
struct LS_vtable LS_vtable_;
// structures
struct LinearSearch
{
  struct LinearSearch_vtable *vptr;
};
struct LS
{
  struct LS_vtable *vptr;
  int * number;
  int size;
};
// vtables structures
struct LinearSearch_vtable
{
};

struct LS_vtable
{
  int (*Start)();
  int (*Print)();
  int (*Search)();
  int (*Init)();
};


// methods
int LS_Start(struct LS * this, int sz)
{
  int aux01;
  int aux02;
  struct LS * Init;
  struct LS * Print;
  struct LS * Search;

  aux01 = (Init=this, Init->vptr->Init(Init, sz));
  aux02 = (Print=this, Print->vptr->Print(Print));
  System_out_println (9999);
  System_out_println ((Search=this, Search->vptr->Search(Search, 8)));
  System_out_println ((Search=this, Search->vptr->Search(Search, 12)));
  System_out_println ((Search=this, Search->vptr->Search(Search, 17)));
  System_out_println ((Search=this, Search->vptr->Search(Search, 50)));
  return 55;
}
int LS_Print(struct LS * this)
{
  int j;

  j = 1;
  while(  j < this->size)
    {
        System_out_println (this->number[j]);
        j = j + 1;
    }
      return 0;
}
int LS_Search(struct LS * this, int num)
{
  int j;
  unsigned char ls01;
  int ifound;
  int aux01;
  int aux02;
  int nt;

  j = 1;
  ls01 = 0;
  ifound = 0;
  while(  j < this->size)
    {
        aux01 = this->number[j];
        aux02 = num + 1;
        if (aux01 < num)
      nt = 0;
    else
      if (!aux01 < aux02)
        nt = 0;
      else
{
                ls01 = 1;
                ifound = 1;
                j = this->size;
        }


        j = j + 1;
    }
      return ifound;
}
int LS_Init(struct LS * this, int sz)
{
  int j;
  int k;
  int aux01;
  int aux02;

  this->size = sz;
  this->number = (int *)malloc(sizeof(int)*sz);
  j = 1;
  k = this->size + 1;
  while(  j < this->size)
    {
        aux01 = 2 * j;
        aux02 = k - 3;
        this->number[j] = aux01 + aux02;
        j = j + 1;
        k = k - 1;
    }
      return 0;
}

// vtables
struct LinearSearch_vtable LinearSearch_vtable_ = 
{
};

struct LS_vtable LS_vtable_ = 
{
  LS_Start,
  LS_Print,
  LS_Search,
  LS_Init,
};


// main method
int Tiger_main ()
{
  struct LS * Start;
  System_out_println ((Start=((struct LS*)(Tiger_new (&LS_vtable_, sizeof(struct LS)))), Start->vptr->Start(Start, 10)));
}




