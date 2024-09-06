<img src="https://github.com/user-attachments/assets/ea52000e-a3e2-4fe8-945a-bc46aab41fc3" width="250px">
<img src="https://github.com/user-attachments/assets/ee772cfe-a6f8-47b3-a470-ee6753317409" width="250px">
<img src="https://github.com/user-attachments/assets/4d090d94-42b8-4942-8995-bc3a3f9f0aae" width="250px">

# Implemented
### List GitHub users.
- Architecture: MVVM
- UI:
  - Implement SearchBar with filter
  - Implement RecyclerView with ListAdapter
  - Implement item view show info including image from “avatar_url”, “login”, and “site_admin”
  - Implement pagination start from "since=0, page size=20" **(Bonus)**
  - Implement clicking Item Open Detailed information **(Bonus)**
- Network Request:
  - Network Request work in background thread
  - Implement connection issues  **(Bonus)**
---
### GitHub User API
-  list: [List users](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#list-users)
  - GET https://api.github.com/users?per_page=20&since=0
- detailed info: [Get a user](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)
  - GET https://api.github.com/users/USERNAME
- detailed info User repos
  - GET https://api.github.com/users/USERNAME/repos

### Third party libs
- [ReactiveX RxJava](https://github.com/ReactiveX/RxJava)
- [Square Adapter-rxjava3](https://github.com/square/retrofit/tree/trunk/retrofit-adapters/rxjava3)
- [Square Retrofit](https://github.com/square/retrofit)
- [Square OkHttp](https://github.com/square/okhttp)
- [Square OkHttp logging-interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
- [BumpTech Glide](https://github.com/bumptech/glide?tab=readme-ov-file)

# Not implemented Bonus
- Unit tests
- UI tests
- Implement Handle caching
