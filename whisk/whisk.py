'''
The following tool helps developers and likewise check their work on MatchaDB by providing an easy
to use CLI interface, using Python 3, to run commands against the DB.
'''
import requests
import sys

# Different constants for the names of different commands used on Whisk.
GET = "GET"
POST = "POST"
UPDATE = "UPDATE"
DELETE = "DELETE"
HELP = "HELP"
EXIT = "EXIT"

# The current default protocol, http.
PROTOCOL = "http://"   # Default is http


def retrieve_command():
    '''
    This method promts the user to provide a command, and then returns that command so that it can
    be used by the rest of the application.
    '''
    print("Please provide one of the following:\nGET, POST, UPDATE, DELETE, HELP, EXIT")
    print("Do note that this is not case sensitive.")

    # Gather and return the command.
    return input("Your Command: ")


def help_command():
    '''
    Allows the user to get more information on different commands to better understand their 
    workings and how to use them.
    '''
    print("What command would you like more info on?\nGET, POST, UPDATE, DELETE, HELP, EXIT")
    print("This is not case sensitive.")

    # Get the selected command.
    selected_command = input("> ").upper()

    # Print help information accordingly.
    if (selected_command == GET):
        # TODO: Rewrite with respect to better input sequence
        print("This command allows users to retrieve data from the DB.")
        print("Below are the promts provided with the GET command:")
        print("From: Provide the name of the table in the database you would like data from.")
        print("Select: A query of format \"key\" \"operation\" \"value\", given over three inputs.")
    elif (selected_command == POST):
        # TODO: Rewrite with respect to better input sequence
        print("This command allows users to insert data into the DB.")
        print("Below are the promts provided with the POST command:")
        print("From: Provide the name of the table in the database you would like to insert data.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
        print("Insert: The action to update the DB in a standard JSON format") 
    elif (selected_command == UPDATE):
        # TODO: Rewrite with respect to better input sequence
        print("This command allows users to update data in the DB.")
        print("Below are the promts provided with the UPDATE command:")
        print("From: Provide the name of the table in the database you would like to update.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
        print("Update: The action to update the DB in the format \"key\" \"operation\" \"value\"")        
    elif (selected_command == DELETE):
        # TODO: Rewrite with respect to better input sequence
        print("This command allows users to remove data from the DB.")
        print("Below are the promts provided with the DELETE command:")
        print("From: Provide the name of the table in the database you would remove data from.")
        print("Select: A query separated by spaces in the format \"key\" \"operation\" \"value\"")
    elif (selected_command == HELP):
        print("After typing help in any casing, when prompted, provide the command of interest.")
    elif (selected_command == EXIT):
        print("Just simply type exit when promted in any casing and you will exit the app.")


def get_command():
    '''
    This method collects the From and Select portions for a GET request, runs it against MatchaDB,
    and prints the response code. It will also print exceptions, given that they occur.
    '''
    # Gather the From portion of the command.
    from_portion = "\"" + input("From: ") + "\""

    # Gather the Select portion of the command.
    spart_one = input("Select (key): ")
    spart_two = input("Select (operation): ")
    spart_three = input("Select (value): ")
    select_portion = "[[\"" + spart_one + "\", \"" + spart_two + "\", \"" + spart_three + "\"]]"

    # Develop the Parameter Values.
    parameter_vals = "{\"From\": " + from_portion + ", \"Select\": " + select_portion + "}"

    try:
        # Make the request and see the response code.
        response = requests.get(PROTOCOL + host + ":" + port + "/", data = repr(parameter_vals))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


def post_command():
    '''
    This method collects the From, Select, and Insert portions for a POST request, runs it against
    MatchaDB, and prints the response code. It will also print exceptions, given that they occur.
    '''
    # Gather the From portion of the command.
    from_portion = "\'" + input("From: ") + "\""

    # Gather the Select portion of the command.
    # TODO: Rewrite better input sequence
    spart_one, spart_two, spart_three = input("Select: ").split()
    select_portion = "[[\"" + spart_one + "\", \"" + spart_two + "\", \"" + spart_three + "\"]]"

    # Gather the Insert portion of the command.
    insert_portion = input("Insert: ")

    # Develop the Parameter Values.
    parameter_vals = {"From": from_portion, "Select": select_portion, "Insert": insert_portion}

    try:
        # Make the request and see the response code.
        response = requests.post(PROTOCOL + host + ":" + port + "/", data = repr(parameter_vals))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


def update_command():
    '''
    This method collects the From, Select, and Update portions for a UPDATE (put) request, runs it 
    against MatchaDB, and prints the response code. It will also print exceptions, given that they 
    occur.
    '''
    # Gather the From portion of the command.
    from_portion = input("From: ")

    # Gather the Select portion of the command.
    # TODO: Rewrite better input sequence
    spart_one, spart_two, spart_three = input("Select: ").split()
    select_portion = "[[\"" + spart_one + "\", \"" + spart_two + "\", \"" + spart_three + "\"]]"

    # Gather the Update portion of the command.
    upart_one, upart_two, upart_three = input("Update: ").split()
    update_portion = "[[\"" + upart_one + "\", \"" + upart_two + "\", \"" + upart_three + "\"]]"

    # Develop the Parameter Values.
    parameter_vals = {"From": from_portion, "Select": select_portion, "Update": update_portion}

    try:
        # Make the request and see the response code.
        response = requests.put(PROTOCOL + host + ":" + port + "/", data = repr(parameter_vals))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


def delete_command():
    '''
    This method collects the From and Select portions for a DELETE request, runs it against 
    MatchaDB, and prints the response code. It will also print exceptions, given that they occur.
    '''   
    # Gather the From portion of the command.
    from_portion = input("From: ")

    # Gather the Select portion of the command.
    # TODO: Rewrite better input sequence
    spart_one, spart_two, spart_three = input("Select: ").split()
    select_portion = "[[\"" + spart_one + "\", \"" + spart_two + "\", \"" + spart_three + "\"]]"

    # Develop the Parameter Values.
    parameter_vals = {"From": from_portion, "Select": select_portion}

    try:
        # Make the request and see the response code.
        response = requests.delete(PROTOCOL + host + ":" + port + "/", data = repr(parameter_vals))
        print(response)
    except requests.exceptions.ConnectionError:
        print("A connection error has occured.")
    except Exception as e:
        print("An unidentified error has occured of type " + type(e) + ".")


def main():
    '''
    The main method of the application.
    '''
    # Start of app
    print("Welcome to Whisk, the MatchaDB Tester!")
    print("SammaTime22, 2021")

    # Set the variables of host and port to be global, and then set them
    global host
    global port
    host = "127.0.0.1"     # Default is localhost
    port = "11150"          # Default is port 8080

    # Get the hostname, if it exists.
    if len(sys.argv) > 2:
        host = sys.argv[1]

    # Get the portname, if it exists.
    if len(sys.argv) > 3:
        port = sys.argv[2]

    while True:
        # Remind the user where the command is currently pointed at.
        print("Using " + PROTOCOL + host + ":" + port + "/")

        # Get the command.
        command_to_use = retrieve_command().upper()

        # Check which command this is for and run said command accordingly.
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


# Bootload the application.
if __name__ == "__main__":
    main()
