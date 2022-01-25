#from dataclasses import dataclass
#from getpass import getpass
from mysql.connector import connect, Error
from datetime import datetime

""" try:
    with connect(
        host="localhost",
        port="3306",
        user="userespet",
        password="pwdespet",
        database="espet"
    ) as connection:
        query = f"SELECT login_token, token_date FROM user WHERE id_user={1}"
        with connection.cursor(buffered=True) as cursor:
            cursor.execute(query)
            result = cursor.fetchone()
            assert(result is not None)
            token = result[0]            
            token_date = result[1]
            today = datetime.now()
            diff = today - token_date
            token_life = int(diff.total_seconds() / 60)
            print(f"The token was created {token_life} min. ago")           
except Error as e:
    print(e)
except AssertionError:
    print("Erreur assert")"""




def bindUserToDevice(user, args):
    print(user)
    print(args)

user_actions = {"BIND" : bindUserToDevice}

words = "BIND MAC-ADDRESS".split(' ')
if len(words) > 1 and words[0] in user_actions:
    response = user_actions[words[0]](1, words)
    print(response) 