# Goal 
## List GitHub users.

### Architecture
- MVVM

### UI 
- Implement SearchBar 
- Implement Show users in list use RecyclerView component 
- In list, item view show info including image from “avatar_url”, “login”, and “site_admin”.

### Network Request 
- Network Request Implement with multi Thread
- Limit results to 100 users in total

### API
- [List users](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#list-users)
  - GET https://api.github.com/users?per_page=100
- [Get a user](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)
  - GET https://api.github.com/users/USERNAME

### Third party libs
- Retrofit
- OkHttp 
- Glide

---
# Bonus (if there is free time)
### UI
- Implement pagination
- Implement Clicking Item Open Detailed information

### Network Request
- Implement Handle caching 
- Implement connection issues

### Test 
- Unit tests
- UI tests