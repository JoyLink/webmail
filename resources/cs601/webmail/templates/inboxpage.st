inboxpage(email, filelists) ::= <<

<script>
    function choose() {
        txt = document.getElementById("options").value;
        //alert(txt);
        if(txt=="Reply")
             window.location.href="/reply?id=$email.id$";

        else
             window.location.href="/forward?id=$email.id$";
        //<a href="/reply?id=$email.id$">
        //<a href="/forward?id=$email.id$">
    }
</script>

<select name='account' id='options' onchange="choose()">
<option > Operations </option>
<option value="Reply"> Reply</option>
<option value="Forward"> Forward</option>
</select>
<p>
From:                               $email.from$
<p>
To:                                 $email.to$
<p>
Subject:                            $email.subject$
<p>
Time:                               $email.time$
<p>
Content:
<div>
                                    $email.contentTEXT$
</div>
<div>
                                    $email.contentHTML$
</div>
<hr>
<div>
                                    $filelists:{fl|
                                    <tr>
                                      <a href="/download?url=$fl.url$&fileName=$fl.name$">$fl.name$</a>
                                    </tr>
                                    }$

</div>

>>
