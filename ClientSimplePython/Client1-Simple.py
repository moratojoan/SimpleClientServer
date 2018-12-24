import sys
import requests
import json


def afegir_usuari(nom_usuari, email, contrasenya):
    dict_usuari = {'nom_usuari': nom_usuari, 'email': email, 'contrasenya': contrasenya}
    lloc = "http://localhost:5000/afegir_usuari"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def login(nom_usuari, contrasenya):
    dict_usuari = {'nom_usuari': nom_usuari, 'contrasenya': contrasenya}
    lloc = "http://localhost:5000/login_usuari"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def logout():
    lloc = "http://localhost:5000/logout"
    r = requests.get(lloc)
    return r.text


def eliminar_usuari(nom_usuari, contrasenya):
    dict_usuari = {'nom_usuari': nom_usuari, 'contrasenya': contrasenya}
    lloc = "http://localhost:5000/eliminar_usuari"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def afegir_nota(nom_usuari, titol_nota, text):
    dict_usuari = {'nom_usuari': nom_usuari, 'titol_nota': titol_nota, 'text': text}
    lloc = "http://localhost:5000/afegir_nota"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def obtenir_nota(nom_usuari, titol_nota):
    dict_usuari = {'nom_usuari': nom_usuari, 'titol_nota': titol_nota}
    lloc = "http://localhost:5000/obtenir_nota"
    r = requests.get(lloc, data=dict_usuari)
    return r.text


def obtenir_totes_notes(nom_usuari):
    dict_usuari = {'nom_usuari': nom_usuari}
    lloc = "http://localhost:5000/obtenir_totes_notes"
    r = requests.get(lloc, data=dict_usuari)
    return r.text


def obtenir_totes_notes_dic(nom_usuari):
    dict_usuari = {'nom_usuari': nom_usuari}
    lloc = "http://localhost:5000/obtenir_totes_notes_dic"
    r = requests.get(lloc, data=dict_usuari)
    return json.loads(r.text)


def modificar_titol_nota(nom_usuari, titol_nota, nou_titol_nota):
    dict_usuari = {'nom_usuari': nom_usuari, 'titol_nota': titol_nota, 'nou_titol_nota': nou_titol_nota}
    lloc = "http://localhost:5000/modificar_titol_nota"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def modificar_text_nota(nom_usuari, titol_nota, nou_text):
    dict_usuari = {'nom_usuari': nom_usuari, 'titol_nota': titol_nota, 'nou_text': nou_text}
    lloc = "http://localhost:5000/modificar_text_nota"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def eliminar_nota(nom_usuari, titol_nota):
    dict_usuari = {'nom_usuari': nom_usuari, 'titol_nota': titol_nota}
    lloc = "http://localhost:5000/eliminar_nota"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def eliminar_totes_notes(nom_usuari):
    dict_usuari = {'nom_usuari': nom_usuari}
    lloc = "http://localhost:5000/eliminar_totes_notes"
    r = requests.post(lloc, data=dict_usuari)
    return r.text


def main():
    while True:
        print('')
        print('*** Client1 Simple ****')
        print('1 - Login')
        print('2 - Registrar-se')
        print('3 - Eliminar un usuari')
        print('4 - Exit')
        i = input('Triar una opció: ')
        if i == '1':
            print('')
            print('*** Login ***')
            nom_usuari = input("Nom d'usuari: ")
            contrasenya = input("Contrasenya: ")
            if login(nom_usuari, contrasenya) == "Login realitzat correctament":
                print("Login realitzat correctament")
                while True:
                    print('')
                    print('*** Bloc de notes de ' + nom_usuari + ' ***')
                    print('1 - Logout')
                    print('2 - Afegir nota')
                    print('3 - Buscar nota')
                    print('4 - Obtenir totes notes')
                    print('4.2 - Obtenir totes notes dic')
                    print('5 - Modificar titol nota')
                    print('6 - Modificar text nota')
                    print('7 - Eliminar nota')
                    print('8 - Eliminar totes les notes')
                    i = input('Triar una opció: ')
                    if i == '1':
                        print(logout())
                        break
                    elif i == '2':
                        titol_nota = input('Titol: ')
                        text = input('Text: ')
                        print(afegir_nota(nom_usuari, titol_nota, text))
                    elif i == '3':
                        titol_nota = input('Titol: ')
                        print(obtenir_nota(nom_usuari, titol_nota))
                    elif i == '4':
                        print(obtenir_totes_notes(nom_usuari))
                    elif i == '4.2':
                        print(obtenir_totes_notes_dic(nom_usuari))
                        a = obtenir_totes_notes_dic(nom_usuari)
                        print(a)
                        for i in a:
                            print("")
                            print("Titol: " + i["Titol"])
                            print("Text: " + i["Text"])
                    elif i == '5':
                        titol_nota = input('Titol nota antic: ')
                        nou_titol_nota = input('Nou titol nota: ')
                        print(modificar_titol_nota(nom_usuari, titol_nota, nou_titol_nota))
                    elif i == '6':
                        titol_nota = input('Titol nota: ')
                        nou_text = input('Nou text: ')
                        print(modificar_text_nota(nom_usuari, titol_nota, nou_text))
                    elif i == '7':
                        titol_nota = input('Titol nota: ')
                        print(eliminar_nota(nom_usuari, titol_nota))
                    elif i == '8':
                        print(eliminar_totes_notes(nom_usuari))
                    else:
                        print('Triar una opció valida.')
                        print('')
            else:
                print("Usuari i/o Contrasenya no vàlids")
        elif i == '2':
            print('')
            print('*** Registrar-se ***')
            nom_usuari = input("Nom d'usuari: ")
            email = input("Email: ")
            contrasenya = input("Contrasenya: ")
            print(afegir_usuari(nom_usuari, email, contrasenya))
        elif i == '3':
            nom_usuari = input("Nom de l'usuari a eliminar: ")
            contrasenya = input('Contrasenya: ')
            print(eliminar_usuari(nom_usuari, contrasenya))
        elif i == '4':
            exit()
        else:
            print('Triar una opció valida.')
            print('')


if __name__ == '__main__':
    main()
