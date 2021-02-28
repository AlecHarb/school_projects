enum StatusEnus {INVALID, VALID, CONSUMED};
typedef struct {
    enum StatusEnus status;
    int data;
}ShmData;