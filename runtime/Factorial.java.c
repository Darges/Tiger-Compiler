// This is automatically generated by the Tiger compiler.
// Do NOT modify!

//declaration
struct Factorial_vtable Factorial_vtable_;
struct Fac_vtable Fac_vtable_;
// structures
struct Factorial
{
  struct Factorial_vtable *vptr;
};
struct Fac
{
  struct Fac_vtable *vptr;
};
// vtables structures
struct Factorial_vtable
{
};

struct Fac_vtable
{
  int (*ComputeFac)();
};


// methods
int Fac_ComputeFac(struct Fac * this, int num)
{
  int num_aux;
  struct Fac * ComputeFac;

  if (num < 1)
    num_aux = 1;
  else
    num_aux = num * (ComputeFac=this, ComputeFac->vptr->ComputeFac(ComputeFac, num - 1));

  return num_aux;
}

// vtables
struct Factorial_vtable Factorial_vtable_ = 
{
};

struct Fac_vtable Fac_vtable_ = 
{
  Fac_ComputeFac,
};


// main method
int Tiger_main ()
{
  struct Fac * ComputeFac;
  System_out_println ((ComputeFac=((struct Fac*)(Tiger_new (&Fac_vtable_, sizeof(struct Fac)))), ComputeFac->vptr->ComputeFac(ComputeFac, 10)));
}




