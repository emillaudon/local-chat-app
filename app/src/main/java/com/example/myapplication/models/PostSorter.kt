package com.example.myapplication.models

class PostSorter {

    companion object {

        fun ArrayList<Post>.bubbleSort() : ArrayList<Post> {
            var swap = true
            while(swap) {
                swap = false
                for(i in 0 until this.size-1) {
                    if(this[i].getDate() < this[i+1].getDate()) {
                        val temp = this[i]
                        this[i] = this[i + 1]
                        this[i + 1] = temp

                        swap = true
                    }
                }
            }
            return this
        }
    }
}