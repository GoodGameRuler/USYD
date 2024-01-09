#ifndef MAB_H
#define MAB_H

typedef struct mab {
    int offset;            // starting address of the memory block
    int size;              // size of the memory block
    int allocated;         // block allocated or not
    struct mab* parent;    // for use in the Buddy binary tree
    struct mab* left_child;// for use in the binary tree
    struct mab* right_child;// for use in the binary tree
} Mab;

typedef Mab* MabPtr;

// Function declarations
MabPtr memMerge(MabPtr m);
MabPtr memSplit(MabPtr m, int size);
MabPtr memAlloc(MabPtr m, int size);
MabPtr memFree(MabPtr m);

#endif
