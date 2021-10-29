package com.gyeongsotone.danplay.model

data class MatchDTO(var matchId : Long? = null,
                    var sports : String? = null,
                    var totalNum : String? = null,
                    var currentNum : String? = null,
                    var place : String? = null,
                    var content : String? = null,
                    var playTime : String? = null,
                    var applyTime : Long? = null,) {
}