#include "mab.h"
#include <stdlib.h>

// Function to merge buddy memory blocks
MabPtr memMerge(MabPtr m) {
    MabPtr parent = m->parent;
    if(parent == NULL) {
        return NULL;
    }

    if (parent->left_child->allocated == 0 &&
        parent->right_child->allocated == 0) {

        // Merge the blocks
        parent->allocated = 0;
        free(parent->right_child);
        free(parent->left_child);
        parent->left_child = NULL;
        parent->right_child = NULL;

        // Recursively merge the parent
        return memMerge(parent);
    }

    return m;
}

// Function to split a memory block
MabPtr memSplit(MabPtr m, int size) {
    
    if(!m)
        return NULL;

    if (m->size / 2 >= size) {
        m->allocated = 1;
        m->left_child = (MabPtr)malloc(sizeof(Mab));
        m->left_child->offset = m->offset;
        m->left_child->size = m->size / 2;
        m->left_child->allocated = 0;
        m->left_child->parent = m;
        m->left_child->left_child = NULL;
        m->left_child->right_child = NULL;

        m->right_child = (MabPtr)malloc(sizeof(Mab));
        m->right_child->offset = m->offset + m->size / 2;
        m->right_child->size = m->size / 2;
        m->right_child->allocated = 0;
        m->right_child->parent = m;
        m->right_child->left_child = NULL;
        m->right_child->right_child = NULL;
        return memSplit(m->left_child, size);

    } else {
        m->allocated = 1;
        return m;
    }

}

// Function to allocate memory block
MabPtr memAlloc(MabPtr m, int size) {

    if(m == NULL)
        return NULL;

    if(m->allocated == 0 && m->size >= size) {
        return m;

    } else {
        
        MabPtr temp = memAlloc(m->left_child, size);

        if(temp)
            return temp;

        temp = memAlloc(m->right_child, size);

        if(temp)
            return temp;
            
    }

    return NULL; // Unable to allocate
}



// Function to free memory block
MabPtr memFree(MabPtr m) {

    if(!m)
        return NULL;
    
    m->allocated = 0;

    if (m->parent != NULL) {
        // Merge buddy blocks
        return memMerge(m);
    }

    return NULL; // Cannot free the root
}

