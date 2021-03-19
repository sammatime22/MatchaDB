'''
The following tool helps developers and likewise check their work on MatchaDB by providing an easy
to use CLI interface, using Python 3, to run commands against the DB.
'''
import requests

GET = "GET"
POST = "POST"
UPDATE = "UPDATE"
DELETE = "DELETE"
HELP = "HELP"
EXIT = "EXIT"

# Retrieve Command and Info
def retrieve_command():
    print("Please provide one of the following:\nGET, POST, UPDATE, DELETE, HELP, EXIT")
    print("Do note that this is not case sensitive.")
    return input("Your Command: ")


# Help Command
def help_command():
    print("What command would you like more info on?\nGET, POST, UPDATE, DELETE, HELP, EXIT")
    print("This is not case sensitive.")
    selected_command = input("> ").upper()
    if (selected_command == GET):
        print("This command allows users to retrieve data from the DB.")
        print("Below are the promts provided with the GET command:")
        print("From: Provide the name of the table in the database you would like data from.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
    elif (selected_command == POST):
        print("This command allows users to insert data into the DB.")
        print("Below are the promts provided with the POST command:")
        print("From: Provide the name of the table in the database you would like to insert data.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
        print("Insert: The action to update the DB in a standard JSON format") 
    elif (selected_command == UPDATE):
        print("This command allows users to update data in the DB.")
        print("Below are the promts provided with the UPDATE command:")
        print("From: Provide the name of the table in the database you would like to update.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
        print("Update: The action to update the DB in the format \"key\" \"operation\" \"value\"")        
    elif (selected_command == DELETE):
        print("This command allows users to remove data from the DB.")
        print("Below are the promts provided with the DELETE command:")
        print("From: Provide the name of the table in the database you would remove data from.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
    elif (selected_command == HELP):
        print("After typing help in any casing, when prompted, provide the command of interest.")
    elif (selected_command == EXIT):
        print("Just simply type exit when promted in any casing and you will exit the app.")


# Run Get Command
def get_command():
    from_portion = input("From: ")
    select_portion = input("Select: ")
    parameter_values = {"From": from_portion, "Select": select_portion}

    try:
        response = requests.get("http://127.0.0.1:8080/", data = str(parameter_values))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")



# Run Post Command
def post_command():
    from_portion = input("From: ")
    select_portion = input("Select: ")
    insert_portion = input("Insert: ")
    parameter_values = {"From": from_portion, "Select": select_portion, "Insert": insert_portion}

    try:
        response = requests.post("http://127.0.0.1:8080/", data = str(parameter_values))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


# Run Update Command
def update_command():
    from_portion = input("From: ")
    select_portion = input("Select: ")
    update_portion = input("Update: ")
    parameter_values = {"From": from_portion, "Select": select_portion, "Update": update_portion}

    try:
        response = requests.put("http://127.0.0.1:8080/", data = str(parameter_values))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


# Run Delete Command
def delete_command():
    from_portion = input("From: ")
    select_portion = input("Select: ")
    parameter_values = {"From": from_portion, "Select": select_portion}

    try:
        response = requests.delete("http://127.0.0.1:8080/", data = str(parameter_values))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")



# Start of app
print("Welcome to Whisk, the MatchaDB Tester!")
print("SammaTime22, 2021")

while True:
    command_to_use = retrieve_command().upper()

    if (command_to_use == GET):
        get_command()
    elif (command_to_use == POST):
        post_command()
    elif (command_to_use == UPDATE):
        update_command()
    elif (command_to_use == DELETE):
        delete_command()
    elif (command_to_use == HELP):
        help_command()
    elif (command_to_use == EXIT):
        break