package com.example.myapplication.models

class PostSorter {

    companion object {

        fun bubbleSort(post: ArrayList<Post>) : ArrayList<Post> {
            var swap = true
            while(swap) {
                swap = false
                for(i in 0 until post.size-1) {
                    if(post[i].getDate() < post[i+1].getDate()) {
                        val temp = post[i]
                        post[i] = post[i + 1]
                        post[i + 1] = temp

                        swap = true
                    }
                }
            }
            return post
        }
    }
}