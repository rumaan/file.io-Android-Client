package com.thecoolguy.rumaan.fileio.data.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Response(
  val success: Boolean,
  val key: String,
  val link: String,
  val expiry: String,
  val message: String,
  val error: Int
) {
  class Deserializer : ResponseDeserializable<Response> {
    override fun deserialize(content: String): Response {
      return Gson().fromJson(content, Response::class.java)
    }
  }
}