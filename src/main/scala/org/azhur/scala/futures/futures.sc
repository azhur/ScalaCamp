case class HttpResponse(body: String)
case class UserDto(id: String)

def callRestApi(url: String): HttpResponse = {
  HttpResponse(s"Got response from $url")
}
def decodeHttpResponse(response: HttpResponse): UserDto = {
  UserDto(response.body)
}

// make 5 requests to the api
val user1 = decodeHttpResponse(callRestApi("url/1"))
val user2 = decodeHttpResponse(callRestApi("url/2"))
val user3 = decodeHttpResponse(callRestApi("url/3"))
val user4 = decodeHttpResponse(callRestApi("url/4"))
val user5 = decodeHttpResponse(callRestApi("url/5"))