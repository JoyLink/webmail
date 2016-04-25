register() ::= <<
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Register</title>
    <script language="javascript">
        function checkname(){
            var div = document.getElementById("div1");
            div.innerHTML = "";
            var name1 = document.form1.text1.value;
            if (name1 == "") {
                div.innerHTML = "Null username";
                document.form1.text1.focus();
                return false;
            }
            if (name1.length < 4 || name1.length > 16) {
                div.innerHTML = "Length of username should be 4~16";
                document.form1.text1.select();
                return false;
            }
            var charname1 = name1.toLowerCase();
            for (var i = 0; i < name1.length; i++) {
                var charname = charname1.charAt(i);
                if (!(charname >= 0 && charname <= 9) && (!(charname >= 'a' && charname <= 'z')) && (charname != '_')) {
                    div.innerHTML = "Invalid username";
                    document.form1.text1.select();
                    return false;
                }
            }
            return true;

        }

        function checkpassword(){
            var div = document.getElementById("div2");
            div.innerHTML = "";
            var password = document.form1.text2.value;
            if (password == "") {
                div.innerHTML = "password shouldn't be null";
                document.form1.text2.focus();
                return false;
            }
            if (password.length < 4 || password.length > 12) {
                div.innerHTML = "length of password should be 4~12";
                document.form1.text2.select();
                return false;
            }
            return true;
        }

        function checkrepassword(){
            var div = document.getElementById("div3");
            div.innerHTML = "";
            var password = document.form1.text2.value;
            var repass = document.form1.text3.value;
            if (repass == "") {
                div.innerHTML = "Null password";
                document.form1.text3.focus();
                return false;
            }
            if (password != repass) {
                div.innerHTML = "confirm your password";
                document.form1.text3.select();
                return false;
            }
            return true;
        }

        function check(){
            if (checkname() && checkpassword() && checkrepassword() ) {
                return true;
            }
            else {
                return false;
            }
        }
    </script>
</head>

<body>
<p align="center">-Registeration-</p>

<hr />
<form name="form1" method="post" action="/doRegister" onsubmit="return check()">
    <table align="center">
        <tr>
            <td>
                UserName:
            </td>
            <td>
                <input id="text1" type="text" name="text1" onblur="check()">
                <div id="div1" style="display:inline">
                </div>
            </td>
        </tr>
        <tr>
            <td>
                PassWord:
            </td>
            <td>
                <input id="text2" type="password" name="text2" onblur="check()">
                <div id="div2" style="display:inline">
                </div>
            </td>
        </tr>
        <tr>
            <td>
                Confirm:
            </td>
            <td>
                <input id="text3" type="password" name="text3" onblur="check()">
                <div id="div3" style="display:inline">
                </div>
            </td>
        </tr>

        <tr align="center">
            <td align="center">
                <input type="submit" value="submit" name="tect6">
            </td>
            <td>
                <input type="reset" value="reset" name="text7">
            </td>
        </tr>
    </table>

</form>
<a href="/">home</a>
</body>
</html>
>>

