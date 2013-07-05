// This is automatically generated by the Tiger compiler.
// Do NOT modify!

//declaration
struct TreeVisitor_vtable TreeVisitor_vtable_;
struct TV_vtable TV_vtable_;
struct Tree_vtable Tree_vtable_;
struct Visitor_vtable Visitor_vtable_;
struct MyVisitor_vtable MyVisitor_vtable_;
// structures
struct TreeVisitor
{
  struct TreeVisitor_vtable *vptr;
};
struct TV
{
  struct TV_vtable *vptr;
};
struct Tree
{
  struct Tree_vtable *vptr;
  struct Tree * left;
  struct Tree * right;
  int key;
  unsigned char has_left;
  unsigned char has_right;
  struct Tree * my_null;
};
struct Visitor
{
  struct Visitor_vtable *vptr;
  struct Tree * l;
  struct Tree * r;
};
struct MyVisitor
{
  struct MyVisitor_vtable *vptr;
  struct Tree * l;
  struct Tree * r;
};
// vtables structures
struct TreeVisitor_vtable
{
};

struct TV_vtable
{
  int (*Start)();
};

struct Tree_vtable
{
  unsigned char (*Init)();
  unsigned char (*SetRight)();
  unsigned char (*SetLeft)();
  struct Tree * (*GetRight)();
  struct Tree * (*GetLeft)();
  int (*GetKey)();
  unsigned char (*SetKey)();
  unsigned char (*GetHas_Right)();
  unsigned char (*GetHas_Left)();
  unsigned char (*SetHas_Left)();
  unsigned char (*SetHas_Right)();
  unsigned char (*Compare)();
  unsigned char (*Insert)();
  unsigned char (*Delete)();
  unsigned char (*Remove)();
  unsigned char (*RemoveRight)();
  unsigned char (*RemoveLeft)();
  int (*Search)();
  unsigned char (*Print)();
  unsigned char (*RecPrint)();
  int (*accept)();
};

struct Visitor_vtable
{
  int (*visit)();
};

struct MyVisitor_vtable
{
  int (*visit)();
};


// methods
int TV_Start(struct TV * this)
{
  struct Tree * root;
  unsigned char ntb;
  int nti;
  struct MyVisitor * v;
  struct Tree * Init;
  struct Tree * Print;
  struct Tree * Insert;
  struct Tree * accept;
  struct Tree * Search;
  struct Tree * Delete;

  root = ((struct Tree*)(Tiger_new (&Tree_vtable_, sizeof(struct Tree))));
  ntb = (Init=root, Init->vptr->Init(Init, 16));
  ntb = (Print=root, Print->vptr->Print(Print));
  System_out_println (100000000);
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 8));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 24));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 4));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 12));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 20));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 28));
  ntb = (Insert=root, Insert->vptr->Insert(Insert, 14));
  ntb = (Print=root, Print->vptr->Print(Print));
  System_out_println (100000000);
  v = ((struct MyVisitor*)(Tiger_new (&MyVisitor_vtable_, sizeof(struct MyVisitor))));
  System_out_println (50000000);
  nti = (accept=root, accept->vptr->accept(accept, v));
  System_out_println (100000000);
  System_out_println ((Search=root, Search->vptr->Search(Search, 24)));
  System_out_println ((Search=root, Search->vptr->Search(Search, 12)));
  System_out_println ((Search=root, Search->vptr->Search(Search, 16)));
  System_out_println ((Search=root, Search->vptr->Search(Search, 50)));
  System_out_println ((Search=root, Search->vptr->Search(Search, 10)));
  ntb = (Delete=root, Delete->vptr->Delete(Delete, 12));
  ntb = (Print=root, Print->vptr->Print(Print));
  System_out_println ((Search=root, Search->vptr->Search(Search, 12)));
  return 0;
}
unsigned char Tree_Init(struct Tree * this, int v_key)
{

  this->key = v_key;
  this->has_left = 0;
  this->has_right = 0;
  return 1;
}
unsigned char Tree_SetRight(struct Tree * this, struct Tree * rn)
{

  this->right = rn;
  return 1;
}
unsigned char Tree_SetLeft(struct Tree * this, struct Tree * ln)
{

  this->left = ln;
  return 1;
}
struct Tree * Tree_GetRight(struct Tree * this)
{

  return this->right;
}
struct Tree * Tree_GetLeft(struct Tree * this)
{

  return this->left;
}
int Tree_GetKey(struct Tree * this)
{

  return this->key;
}
unsigned char Tree_SetKey(struct Tree * this, int v_key)
{

  this->key = v_key;
  return 1;
}
unsigned char Tree_GetHas_Right(struct Tree * this)
{

  return this->has_right;
}
unsigned char Tree_GetHas_Left(struct Tree * this)
{

  return this->has_left;
}
unsigned char Tree_SetHas_Left(struct Tree * this, unsigned char val)
{

  this->has_left = val;
  return 1;
}
unsigned char Tree_SetHas_Right(struct Tree * this, unsigned char val)
{

  this->has_right = val;
  return 1;
}
unsigned char Tree_Compare(struct Tree * this, int num1, int num2)
{
  unsigned char ntb;
  int nti;

  ntb = 0;
  nti = num2 + 1;
  if (num1 < num2)
    ntb = 0;
  else
    if (!num1 < nti)
      ntb = 0;
    else
      ntb = 1;


  return ntb;
}
unsigned char Tree_Insert(struct Tree * this, int v_key)
{
  struct Tree * new_node;
  unsigned char ntb;
  struct Tree * current_node;
  unsigned char cont;
  int key_aux;
  struct Tree * Init;
  struct Tree * GetKey;
  struct Tree * GetHas_Left;
  struct Tree * GetLeft;
  struct Tree * SetHas_Left;
  struct Tree * SetLeft;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;
  struct Tree * SetHas_Right;
  struct Tree * SetRight;

  new_node = ((struct Tree*)(Tiger_new (&Tree_vtable_, sizeof(struct Tree))));
  ntb = (Init=new_node, Init->vptr->Init(Init, v_key));
  current_node = this;
  cont = 1;
  while(  cont)
    {
        key_aux = (GetKey=current_node, GetKey->vptr->GetKey(GetKey));
        if (v_key < key_aux)
{
            if ((GetHas_Left=current_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
        current_node = (GetLeft=current_node, GetLeft->vptr->GetLeft(GetLeft));
      else
{
                cont = 0;
                ntb = (SetHas_Left=current_node, SetHas_Left->vptr->SetHas_Left(SetHas_Left, 1));
                ntb = (SetLeft=current_node, SetLeft->vptr->SetLeft(SetLeft, new_node));
        }

      }
    else
{
            if ((GetHas_Right=current_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
        current_node = (GetRight=current_node, GetRight->vptr->GetRight(GetRight));
      else
{
                cont = 0;
                ntb = (SetHas_Right=current_node, SetHas_Right->vptr->SetHas_Right(SetHas_Right, 1));
                ntb = (SetRight=current_node, SetRight->vptr->SetRight(SetRight, new_node));
        }

      }

    }
      return 1;
}
unsigned char Tree_Delete(struct Tree * this, int v_key)
{
  struct Tree * current_node;
  struct Tree * parent_node;
  unsigned char cont;
  unsigned char found;
  unsigned char ntb;
  unsigned char is_root;
  int key_aux;
  struct Tree * GetKey;
  struct Tree * GetHas_Left;
  struct Tree * GetLeft;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;
  struct Tree * Remove;

  current_node = this;
  parent_node = this;
  cont = 1;
  found = 0;
  is_root = 1;
  while(  cont)
    {
        key_aux = (GetKey=current_node, GetKey->vptr->GetKey(GetKey));
        if (v_key < key_aux)
      if ((GetHas_Left=current_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
{
                parent_node = current_node;
                current_node = (GetLeft=current_node, GetLeft->vptr->GetLeft(GetLeft));
        }
      else
        cont = 0;

    else
      if (key_aux < v_key)
        if ((GetHas_Right=current_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
{
                    parent_node = current_node;
                    current_node = (GetRight=current_node, GetRight->vptr->GetRight(GetRight));
          }
        else
          cont = 0;

      else
{
                if (is_root)
          if (!(GetHas_Right=current_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)) && !(GetHas_Left=current_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
            ntb = 1;
          else
            ntb = (Remove=this, Remove->vptr->Remove(Remove, parent_node, current_node));

        else
          ntb = (Remove=this, Remove->vptr->Remove(Remove, parent_node, current_node));

                found = 1;
                cont = 0;
        }


        is_root = 0;
    }
      return found;
}
unsigned char Tree_Remove(struct Tree * this, struct Tree * p_node, struct Tree * c_node)
{
  unsigned char ntb;
  int auxkey1;
  int auxkey2;
  struct Tree * GetHas_Left;
  struct Tree * RemoveLeft;
  struct Tree * GetHas_Right;
  struct Tree * RemoveRight;
  struct Tree * GetKey;
  struct Tree * GetLeft;
  struct Tree * Compare;
  struct Tree * SetLeft;
  struct Tree * SetHas_Left;
  struct Tree * SetRight;
  struct Tree * SetHas_Right;

  if ((GetHas_Left=c_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
    ntb = (RemoveLeft=this, RemoveLeft->vptr->RemoveLeft(RemoveLeft, p_node, c_node));
  else
    if ((GetHas_Right=c_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
      ntb = (RemoveRight=this, RemoveRight->vptr->RemoveRight(RemoveRight, p_node, c_node));
    else
{
            auxkey1 = (GetKey=c_node, GetKey->vptr->GetKey(GetKey));
            auxkey2 = (GetKey=(GetLeft=p_node, GetLeft->vptr->GetLeft(GetLeft)), GetKey->vptr->GetKey(GetKey));
            if ((Compare=this, Compare->vptr->Compare(Compare, auxkey1, auxkey2)))
{
                ntb = (SetLeft=p_node, SetLeft->vptr->SetLeft(SetLeft, this->my_null));
                ntb = (SetHas_Left=p_node, SetHas_Left->vptr->SetHas_Left(SetHas_Left, 0));
        }
      else
{
                ntb = (SetRight=p_node, SetRight->vptr->SetRight(SetRight, this->my_null));
                ntb = (SetHas_Right=p_node, SetHas_Right->vptr->SetHas_Right(SetHas_Right, 0));
        }

      }


  return 1;
}
unsigned char Tree_RemoveRight(struct Tree * this, struct Tree * p_node, struct Tree * c_node)
{
  unsigned char ntb;
  struct Tree * GetHas_Right;
  struct Tree * SetKey;
  struct Tree * GetRight;
  struct Tree * GetKey;
  struct Tree * SetRight;
  struct Tree * SetHas_Right;

  while(  (GetHas_Right=c_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
    {
        ntb = (SetKey=c_node, SetKey->vptr->SetKey(SetKey, (GetKey=(GetRight=c_node, GetRight->vptr->GetRight(GetRight)), GetKey->vptr->GetKey(GetKey))));
        p_node = c_node;
        c_node = (GetRight=c_node, GetRight->vptr->GetRight(GetRight));
    }
        ntb = (SetRight=p_node, SetRight->vptr->SetRight(SetRight, this->my_null));
        ntb = (SetHas_Right=p_node, SetHas_Right->vptr->SetHas_Right(SetHas_Right, 0));
      return 1;
}
unsigned char Tree_RemoveLeft(struct Tree * this, struct Tree * p_node, struct Tree * c_node)
{
  unsigned char ntb;
  struct Tree * GetHas_Left;
  struct Tree * SetKey;
  struct Tree * GetLeft;
  struct Tree * GetKey;
  struct Tree * SetLeft;
  struct Tree * SetHas_Left;

  while(  (GetHas_Left=c_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
    {
        ntb = (SetKey=c_node, SetKey->vptr->SetKey(SetKey, (GetKey=(GetLeft=c_node, GetLeft->vptr->GetLeft(GetLeft)), GetKey->vptr->GetKey(GetKey))));
        p_node = c_node;
        c_node = (GetLeft=c_node, GetLeft->vptr->GetLeft(GetLeft));
    }
        ntb = (SetLeft=p_node, SetLeft->vptr->SetLeft(SetLeft, this->my_null));
        ntb = (SetHas_Left=p_node, SetHas_Left->vptr->SetHas_Left(SetHas_Left, 0));
      return 1;
}
int Tree_Search(struct Tree * this, int v_key)
{
  struct Tree * current_node;
  int ifound;
  unsigned char cont;
  int key_aux;
  struct Tree * GetKey;
  struct Tree * GetHas_Left;
  struct Tree * GetLeft;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;

  current_node = this;
  cont = 1;
  ifound = 0;
  while(  cont)
    {
        key_aux = (GetKey=current_node, GetKey->vptr->GetKey(GetKey));
        if (v_key < key_aux)
      if ((GetHas_Left=current_node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
        current_node = (GetLeft=current_node, GetLeft->vptr->GetLeft(GetLeft));
      else
        cont = 0;

    else
      if (key_aux < v_key)
        if ((GetHas_Right=current_node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
          current_node = (GetRight=current_node, GetRight->vptr->GetRight(GetRight));
        else
          cont = 0;

      else
{
                ifound = 1;
                cont = 0;
        }


    }
      return ifound;
}
unsigned char Tree_Print(struct Tree * this)
{
  unsigned char ntb;
  struct Tree * current_node;
  struct Tree * RecPrint;

  current_node = this;
  ntb = (RecPrint=this, RecPrint->vptr->RecPrint(RecPrint, current_node));
  return 1;
}
unsigned char Tree_RecPrint(struct Tree * this, struct Tree * node)
{
  unsigned char ntb;
  struct Tree * GetHas_Left;
  struct Tree * RecPrint;
  struct Tree * GetLeft;
  struct Tree * GetKey;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;

  if ((GetHas_Left=node, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
{
        ntb = (RecPrint=this, RecPrint->vptr->RecPrint(RecPrint, (GetLeft=node, GetLeft->vptr->GetLeft(GetLeft))));
    }
  else
    ntb = 1;

  System_out_println ((GetKey=node, GetKey->vptr->GetKey(GetKey)));
  if ((GetHas_Right=node, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
{
        ntb = (RecPrint=this, RecPrint->vptr->RecPrint(RecPrint, (GetRight=node, GetRight->vptr->GetRight(GetRight))));
    }
  else
    ntb = 1;

  return 1;
}
int Tree_accept(struct Tree * this, struct Visitor * v)
{
  int nti;
  struct Visitor * visit;

  System_out_println (333);
  nti = (visit=v, visit->vptr->visit(visit, this));
  return 0;
}
int Visitor_visit(struct Visitor * this, struct Tree * n)
{
  int nti;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;
  struct Tree * accept;
  struct Tree * GetHas_Left;
  struct Tree * GetLeft;

  if ((GetHas_Right=n, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
{
        this->r = (GetRight=n, GetRight->vptr->GetRight(GetRight));
        nti = (accept=this->r, accept->vptr->accept(accept, this));
    }
  else
    nti = 0;

  if ((GetHas_Left=n, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
{
        this->l = (GetLeft=n, GetLeft->vptr->GetLeft(GetLeft));
        nti = (accept=this->l, accept->vptr->accept(accept, this));
    }
  else
    nti = 0;

  return 0;
}
int MyVisitor_visit(struct MyVisitor * this, struct Tree * n)
{
  int nti;
  struct Tree * GetHas_Right;
  struct Tree * GetRight;
  struct Tree * accept;
  struct Tree * GetKey;
  struct Tree * GetHas_Left;
  struct Tree * GetLeft;

  if ((GetHas_Right=n, GetHas_Right->vptr->GetHas_Right(GetHas_Right)))
{
        this->r = (GetRight=n, GetRight->vptr->GetRight(GetRight));
        nti = (accept=this->r, accept->vptr->accept(accept, this));
    }
  else
    nti = 0;

  System_out_println ((GetKey=n, GetKey->vptr->GetKey(GetKey)));
  if ((GetHas_Left=n, GetHas_Left->vptr->GetHas_Left(GetHas_Left)))
{
        this->l = (GetLeft=n, GetLeft->vptr->GetLeft(GetLeft));
        nti = (accept=this->l, accept->vptr->accept(accept, this));
    }
  else
    nti = 0;

  return 0;
}

// vtables
struct TreeVisitor_vtable TreeVisitor_vtable_ = 
{
};

struct TV_vtable TV_vtable_ = 
{
  TV_Start,
};

struct Tree_vtable Tree_vtable_ = 
{
  Tree_Init,
  Tree_SetRight,
  Tree_SetLeft,
  Tree_GetRight,
  Tree_GetLeft,
  Tree_GetKey,
  Tree_SetKey,
  Tree_GetHas_Right,
  Tree_GetHas_Left,
  Tree_SetHas_Left,
  Tree_SetHas_Right,
  Tree_Compare,
  Tree_Insert,
  Tree_Delete,
  Tree_Remove,
  Tree_RemoveRight,
  Tree_RemoveLeft,
  Tree_Search,
  Tree_Print,
  Tree_RecPrint,
  Tree_accept,
};

struct Visitor_vtable Visitor_vtable_ = 
{
  Visitor_visit,
};

struct MyVisitor_vtable MyVisitor_vtable_ = 
{
  MyVisitor_visit,
};


// main method
int Tiger_main ()
{
  struct TV * Start;
  System_out_println ((Start=((struct TV*)(Tiger_new (&TV_vtable_, sizeof(struct TV)))), Start->vptr->Start(Start)));
}



