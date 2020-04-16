# Spring-security-project
BadCredentialsExceptions при введении корректных данных, не работает http.authorizeRequests(), в остальном вроде инициализируется

Update

Разобрался, в authorize не было permitAll на /login, а BadCredentials вылетал из-за несовпадающего хэша, который по ошибке дважды брался при регистрации.
