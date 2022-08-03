package com.example.debri_lize.view.class_

import com.example.debri_lize.response.Lecture

interface LectureFilterView {
    fun onLectureFilterSuccess(code: Int, result: List<Lecture>)
    fun onLectureFilterFailure(code : Int)
}