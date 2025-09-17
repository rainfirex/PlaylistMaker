package com.practicum.playlistmaker.responses

import com.practicum.playlistmaker.models.Track

class TrackSearchResponse(val resultCount: Int, val results: List<Track>, ) {
}