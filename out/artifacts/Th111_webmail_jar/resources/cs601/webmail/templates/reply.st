reply(email) ::= <<
<html>


<head>

</head>

<body>

<form name="addacount" method="post" action="/sendEmail" enctype="multipart/form-data">
    <table align="center">
        <tr>
            <td>
                To:
            </td>
            <td>
                <input id="text1" type="text" name="mailto" style="width: 500px;" value=$email.from$ readonly="readonly">
                <div id="div1" style="display:inline">
                </div>
            </td>
        </tr>

        <tr>
            <td>
                Subject:
            </td>
            <td>
                <input id="text3" type="text" name="subject" style="width: 500px;" value="Re:$email.subject$" readonly="readonly">
                <div id="div3" style="display:inline"></div>
            </td>
            </tr>

        <tr>
            <td>
                Content:
            </td>
            <td>
                <textarea name="content" style="width: 500px;height: 500px;" >
                    $email.contentTEXT$
                </textarea>
            </td>
        </tr>


        <tr align="right">
            <td align="center">
                <input type="submit" value="send" name="tect6">
            </td>

        </tr>
    </table>
</form>

</body>
</html>
>>