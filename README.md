# IdentifyAdminJna
Finding Windows Admin using JNA java 8

Alternate way implementing the processbuilder to execute below command 
in command line

**net user**

And check for output contains the _Administrator_ String

```
   public static  boolean isAdmin() {
               StringBuilder outputbuilder = new StringBuilder();
           try {
               ProcessBuilder builder = new ProcessBuilder(
                       "cmd.exe","/c" ,"net user");
               builder.redirectErrorStream(true);
               Process p = builder.start();
               BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
               String line;
               while (true) {
                   line = r.readLine();
                   if (line == null) { break; }
                   outputbuilder.append(line);
               }
           } catch (IOException e) {
               e.printStackTrace();
               return false;
           }
           System.out.println(outputbuilder.toString());
           return outputbuilder.toString().contains("Administrator");
       }
```