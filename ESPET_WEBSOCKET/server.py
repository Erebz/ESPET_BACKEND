import asyncio
import websockets
from mysql.connector import connect, Error
from datetime import datetime

host = "192.168.1.41"
port = 9999

MAX_TOKEN_LIFE = 120 # Token expires after 120 min

clients = []
users = dict()
devices = dict()

map_user_device = dict()
#map_device_espet = dict()

# User actions
async def bindUserToDevice(id, args):
    try:
        assert(args is not None and len(args) == 2)                      
        mac = args[1]
        assert(mac in devices)
        assert(isUserOf(id, mac))
        ids = getKeyOfValue(map_user_device, mac)
        if len(ids) != 0: return "DEVICE ALREADY BINDED"
        map_user_device[id] = mac
        return "CONNECTED"
    except:
        return False

async def unbindUser(id, args):
    try:
        assert(args is not None and len(args) == 1)                      
        del map_user_device[id]
        return True
    except:
        return False   

async def loadEspetToDevice(id, args):
    try:
        assert(args is not None and len(args) == 2)
        assert(id in map_user_device)
        mac = map_user_device[id]
        assert(mac in devices)
        device = devices[mac]
        espet = args[1]
        await device.send(f"LOAD {espet}")
        return "LOAD COMMAND SENT"
    except:
        return False
    
async def pingDevices(id, args):
    try:
        assert(args is not None and len(args) >= 2)
        pings = ""
        nMacs = len(args) - 1
        print(f"Pinging {args[1:]}")
        for mac in args[1:]:
            if mac in devices:
                print(isUserOf(id, mac))
                if isUserOf(id, mac):
                    pings += "UP "
                else:
                    pings += "ERROR "
            else :
                pings += "DOWN "
        return pings
    except:
        return False

async def sendAction(id, args):
    try:
        assert(args is not None and len(args) == 2)
        assert(id in map_user_device)
        mac = map_user_device[id]
        assert(mac in devices)
        assert(isUserOf(id, mac))
        device = devices[mac]
        action = args[1]
        assert (action in ['FEED', 'TREAT', 'SLEEP', 'PLAY'])
        await device.send(f"ACTION {action}")
        #return f"{action} ACTION SENT"
    except:
        return False

async def isInGame(id, args):
    try:
        assert(args is not None and len(args) == 1)
        assert(id in map_user_device)
        mac = map_user_device[id]
        assert(mac in devices)
        assert(isUserOf(id, mac))
        device = devices[mac]
        await device.send(f"GAME?")
        #return f"GAME? PING SENT"
    except:
        return False

async def sendSaveCommand(id, args):
    try:
        assert(args is not None and len(args) == 1)
        assert(id in map_user_device)
        mac = map_user_device[id]
        assert(mac in devices)
        assert(isUserOf(id, mac))
        device = devices[mac]
        await device.send(f"SAVE")
        #return f"GAME? PING SENT"
    except:
        return False

async def getInfos(id, args):
    try:
        assert(args is not None and len(args) == 1)
        assert(id in map_user_device)
        mac = map_user_device[id]
        assert(mac in devices)
        assert(isUserOf(id, mac))
        device = devices[mac]
        await device.send(f"INFO")
        #return f"GAME? PING SENT"
    except:
        return False

user_actions = {"BIND" : bindUserToDevice, "LOAD" : loadEspetToDevice, "ACTION" : sendAction, "PING" : pingDevices, "UNBIND" : unbindUser, 
"GAME?" : isInGame, "SAVE" : sendSaveCommand, "INFO" : getInfos}

# Device actions
async def loadEspet(mac, args):
    try:
        assert(args is not None and len(args) == 2)
        id = getKeyOfValue(map_user_device, mac)
        assert(len(id) == 1)
        id = id[0]
        assert(id in map_user_device)
        user = users[id]
        assert(isUserOf(id, mac))
        load = args[1]
        await user.send(f"LOAD {load}")
    except:
        return False

async def saveEspet(mac, args):
    try:
        assert(args is not None and len(args) == 2)
        id = getKeyOfValue(map_user_device, mac)
        assert(len(id) == 1)
        id = id[0]
        assert(id in map_user_device)
        user = users[id]
        assert(isUserOf(id, mac))
        save = args[1]
        await user.send(f"SAVE {save}")
    except:
        return False

async def isDeviceInGame(mac, args):
    try:
        assert(args is not None and len(args) == 2)
        id = getKeyOfValue(map_user_device, mac)
        assert(len(id) == 1)
        id = id[0]
        assert(id in map_user_device)
        user = users[id]
        assert(isUserOf(id, mac))
        inGame = args[1]
        await user.send(f"GAME? {inGame}")
    except:
        return False

async def sendInfos(mac, args):
    try:
        assert(args is not None and len(args) == 2)
        id = getKeyOfValue(map_user_device, mac)
        assert(len(id) == 1)
        id = id[0]
        assert(id in map_user_device)
        user = users[id]
        assert(isUserOf(id, mac))
        infos = args[1]
        await user.send(f"{infos}")
    except:
        return False

async def answerAction(mac, args):
    try:
        assert(args is not None and len(args) == 2)
        id = getKeyOfValue(map_user_device, mac)
        assert(len(id) == 1)
        id = id[0]
        assert(id in map_user_device)
        user = users[id]
        assert(isUserOf(id, mac))
        answer = args[1]
        await user.send(f"ACTION {answer}")
    except:
        return False

device_actions = {"LOAD" : loadEspet, "SAVE" : saveEspet, "GAME?" : isDeviceInGame, "ACTION" : answerAction, "INFO" : sendInfos}

async def welcome(client, path):
    try:
        clients.append(client)
        print("Incomming connection. Resolving client type...")
        await client.send("AUTH?")
        
        args = await client.recv()
        args = args.split(" ")
        print(f"Received : {args}")

        if len(args) == 3:
            type = args[0]
            id = args[1]
            token = args[2]

            if type == "ESP32":
                if not checkToken("device", id, token): 
                    print("Authentication failed.")
                    await client.send("ERROR")
                    return
                # check if already connected
                if id in devices: return
                devices[id] = client
                await handleDevice(client, id)
            elif type == "USER":
                if not checkToken("user", id, token): 
                    await client.send("ERROR")
                    return
                # check if already connected
                if id in users: return
                users[id] = client
                await handleUser(client, id)
            else:
                print("Unknown type.")
                await client.send("TYPE ERROR")
                return
        else:
            print("Bad request.")
            await client.send("Bad request (expected 3 arguments : TYPE + ID + TOKEN)")
    except:
        return
    finally:
        clients.remove(client)



async def handleDevice(device, mac):
    try:
        await device.send("OK")
        async for message in device:
            if len(message) > 0:
                words = message.split(' ')
                if len(words) >= 1 and words[0] in device_actions:
                    response = await device_actions[words[0]](mac, words)
                    if type(response) == str:
                        await device.send(response)                    
                    elif type(response) == bool:
                        send = "OK" if response else "ERROR"
                        await device.send(send)                    
                else: await device.send("COMMAND NOT FOUND")
    except:
        return
    finally:
        macs = getKeyOfValue(devices, device)
        for key in macs: 
            # ping user(s)
            ids = getKeyOfValue(map_user_device, key)
            for id in ids:
                if id in users:
                    user = users[id]
                    del map_user_device[id]
                    await user.send(f"DISCONNECTED {mac}")
            # disconnect device
            del devices[key]
        print(f"Device disconnect ({mac})")

async def handleUser(user, id):
    try:
        await user.send("OK")
        async for message in user:
            if len(message) > 0:
                words = message.split(' ')
                if len(words) >= 1 and words[0] in user_actions:
                    response = await user_actions[words[0]](id, words)
                    if type(response) == str:
                        await user.send(response)                    
                    elif type(response) == bool:
                        send = "OK" if response else "ERROR"
                        await user.send(send)                    
                else: await user.send("COMMAND NOT FOUND")
    except:
        return
    finally:
        id = getKeyOfValue(users, user)
        for key in id: 
            if key in map_user_device : del map_user_device[key]
            del users[key]
            print(f"User disconnect ({key})")       


def getKeyOfValue(d, value):
    return [k for k, v in d.items() if v == value]

async def main():
    async with websockets.serve(welcome, host, port):
        print(f"Server launched on {host}:{port}.")
        await asyncio.Future()  # run forever

def checkToken(type, id, token):
    ##DEBUG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    return True
    try:
        with connect(
            host="localhost",
            port="3306",
            user="userespet",
            password="pwdespet",
            database="espet"
        ) as connection:
            if type == "device":
                query = f"SELECT login_token, token_date FROM device WHERE mac='{id}'"
            elif type == "user":
                query = f"SELECT login_token, token_date FROM user WHERE id_user={id}"
            else: return False
            with connection.cursor(buffered=True) as cursor:
                cursor.execute(query)
                result = cursor.fetchone()
                assert(result is not None)
                fetched_token = result[0]            
                token_date = result[1]
                assert(token == fetched_token)
                today = datetime.now()
                diff = today - token_date
                token_life = int(diff.total_seconds() / 60)
                print(f"The token was created {token_life} min. ago")           
                return token_life <= MAX_TOKEN_LIFE
    except Error as e:
        print(e)
        return False
    except AssertionError:
        print("Unable to authenticate.")
        return False

def isUserOf(id, mac):
    try:
        with connect(
            host="localhost",
            port="3306",
            user="userespet",
            password="pwdespet",
            database="espet"
        ) as connection:
            query = f"SELECT id_user FROM device WHERE mac='{mac}'"
            with connection.cursor(buffered=True) as cursor:
                cursor.execute(query)
                result = cursor.fetchone()
                assert(result is not None)
                fetched_id = result[0]          
                return int(id) == int(fetched_id)
    except Error as e:
        print(e)
        return False
    except AssertionError:
        return False 


if __name__ == "__main__":
    asyncio.run(main())
