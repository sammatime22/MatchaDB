'''
The following tool helps developers and likewise check their work on MatchaDB by providing an easy
to use CLI interface, using Python 3, to run commands against the DB.
'''
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



# Run Get Command


# Run Post Command


# Run Update Command


# Run Delete Command




# Start of app
print("Welcome to Whisk, the MatchaDB Tester!")
print("SammaTime22, 2021")

while True:
    command_to_use = retrieve_command().upper()

    if (command_to_use == EXIT):
        break