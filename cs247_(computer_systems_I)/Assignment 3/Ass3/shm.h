enum StatusEnus {INVALID, VALID, CONSUMED};
struct ShmData{
    enum StatusEnus status;
    int data;
} ;