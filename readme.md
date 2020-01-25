- Akka/Actor greatly simplifies the multi thread player simulation, therefore I am using it in this project,
 however I am very new to it, so I am sure I have done a lot things wrong, but it seems to work :)
- I didn't implement shutDown/restart, so when the game ends, it will just stuck there.
- PlayerA still have some advantage even using Akka/Actor, due to message are always sent to PlayerA first

- What will I do if given more time:
1. I am not super happy about the internal mutable states in GameBoard class, which makes testing difficult and harder to debug,
2. I didn't write unit tests for everything due to time constraint(Also partly because GameBoard has internal mutable state and
 Player are doing side effects which make testing difficult)
3. I put some comments around key places as we don't know each other's coding style, but I didn't explain everything,
 as I believe the code here isn't too complicated, and should all be readable and understandable, I also believe good code should be self documented.