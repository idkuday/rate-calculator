<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insurance Rate Calculator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        form input, form select {
            padding: 5px;
            margin: 5px;
        }
        button {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            $("form").submit(function(event) {
                event.preventDefault();  
                var formData = {
                    zipCode: $("#zipCode").val(),
                    age: $("#age").val(),
                    gender: $("#gender").val()
                };

                $.ajax({
                    type: "POST",
                    url: "/calculateRate",
                    data: formData,
                    success: function(response) {
                        var resultHtml = ;
                        resultHtml += "Rate : "+response;
                        $("#result").html(resultHtml);
                    },
                    error: function() {
                        alert("An error occurred while calculating the rates.");
                    }
                });
            });
        });
    </script>
</head>
<body>
    <h1>Insurance Rate Calculator</h1>
    <form>
        <label for="zipCode">Zip Code:</label>
        <input type="text" id="zipCode" name="zipCode" required>
        <br><br>

        <label for="age">Age:</label>
        <input type="number" id="age" name="age" required>
        <br><br>

        <label for="gender">Gender:</label>
        <select id="gender" name="gender">
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>
        <br><br>

        <button type="submit">Calculate Rate</button>
    </form>

    <div id="result"></div>
</body>
</html>
