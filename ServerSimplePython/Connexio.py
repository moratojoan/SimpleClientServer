from flask import Blueprint, request, session, g, current_app
from BaseDades import Usuari, BlocNotes
import json

CONNEXIO = Blueprint("connexio_api", __name__)


# FUNCIONS PAR TOT
def to_json(data):
    """
    Converts the data to json applying the module settings.
    """
    settings = current_app.config.get('RESTFUL_JSON', {})
    if current_app.debug:
        settings.setdefault('indent', 4)
        settings.setdefault('sort_keys', True)
    return json.dumps(data, **settings)  # json.dumps serveix per serialitzar i enviar informació


# RELACIONATS AMB L'USUARI
@CONNEXIO.route('/afegir_usuari', methods=['POST'])
def afegir_usuari():
    nom_usuari = request.form['nom_usuari']
    email = request.form['email']
    contrasenya = request.form['contrasenya']
    usuari_existeix = Usuari.obtenir_usuari(nom_usuari)
    if usuari_existeix is None:
        Usuari.afegir_usuari(nom_usuari, email, contrasenya)
        return nom_usuari + ' afegit'
    else:
        return "Ja existeix un usuari amb aquest nom"


def usuari_correcte(nom_usuari, contrasenya, nom_usuari_bd, contrasenya_bd):
    return nom_usuari == nom_usuari_bd and contrasenya == contrasenya_bd


def login_valid(nom_usuari, contrasenya):
    usuari_existeix = Usuari.obtenir_usuari(nom_usuari)
    if usuari_existeix is None:
        return False
    else:
        return usuari_correcte(nom_usuari, contrasenya, usuari_existeix.nom_usuari, usuari_existeix.contrasenya)


@CONNEXIO.route('/login_usuari', methods=['POST'])
def login_usuari():
    nom_usuari = request.form['nom_usuari']
    contrasenya = request.form['contrasenya']

    if login_valid(nom_usuari, contrasenya):
        return "Login realitzat correctament"
    else:
        return "Usuari i/o Contrasenya no vàlids"


@CONNEXIO.route('/logout')
def logout():
    session.clear()
    return "Logout realitzat correctament"


@CONNEXIO.route('/eliminar_usuari', methods=['POST'])
def eliminar_usuari():
    nom_usuari = request.form['nom_usuari']
    contrasenya = request.form['contrasenya']

    if login_valid(nom_usuari, contrasenya):
        Usuari.eliminar_usuari(nom_usuari)
        logout()
        return "Usuari eliminat correctament"
    else:
        return "Usuari i/o Contrasenya no vàlids"


# RELACIONATS AMB LES NOTES
@CONNEXIO.route('/afegir_nota', methods=['POST'])
def afegir_nota():
    nom_usuari = request.form['nom_usuari']
    titol_nota = request.form['titol_nota']
    text = request.form['text']

    BlocNotes.afegir_nota(nom_usuari, titol_nota, text)
    return "La nota s'ha afegit correctament"


@CONNEXIO.route('/obtenir_nota', methods=['GET'])
def obtenir_nota():
    nom_usuari = request.form['nom_usuari']
    titol_nota = request.form['titol_nota']

    nota = BlocNotes.obtenir_nota(nom_usuari, titol_nota)
    if nota is not None:
        return "Text: " + nota.text
    else:
        return "No hi ha cap nota amb aquest titol"


@CONNEXIO.route('/obtenir_nota_dic', methods=['GET'])
def obtenir_nota_dic():
    # nom_usuari = request.form['nom_usuari']
    # titol_nota = request.form['titol_nota']
    nom_usuari = request.args.get('nom_usuari')
    titol_nota = request.args.get('titol_nota')

    nota = BlocNotes.obtenir_nota(nom_usuari, titol_nota)
    if nota is not None:
        return to_json([dic_seguent_nota(nota)])
    else:
        return to_json([])


def seguent_nota(i):
    return "Titol: " + i.titol_nota + "\n" + \
           "Text: " + i.text + "\n"


@CONNEXIO.route('/obtenir_totes_notes', methods=['GET'])
def obtenir_totes_notes():
    nom_usuari = request.form['nom_usuari']

    llista_notes = BlocNotes.obtenir_totes_notes(nom_usuari)
    notes = ""
    for i in llista_notes:
        notes = notes + "\n" + seguent_nota(i)
    if notes == "":
        notes = "No hi ha cap nota!"
    return notes


def dic_seguent_nota(i):
    return {"Titol": i.titol_nota,
            "Text": i.text}


@CONNEXIO.route('/obtenir_totes_notes_dic', methods=['GET'])
def obtenir_totes_notes_dic():
    nom_usuari = request.args.get('nom_usuari')
    if nom_usuari is None:
        nom_usuari = request.form['nom_usuari']
    llista_notes = BlocNotes.obtenir_totes_notes(nom_usuari)
    dic_notes = []
    for i in llista_notes:
        dic_notes.append(dic_seguent_nota(i))
    return to_json(dic_notes)


@CONNEXIO.route('/modificar_titol_nota', methods=['POST'])
def modificar_titol_nota():
    nom_usuari = request.form['nom_usuari']
    titol_nota = request.form['titol_nota']
    nou_titol_nota = request.form['nou_titol_nota']

    if BlocNotes.modificar_titol_nota(nom_usuari, titol_nota, nou_titol_nota):
        return "Titol modificat correctament"
    else:
        return "No hi ha cap nota amb aquest titol"


@CONNEXIO.route('/modificar_text_nota', methods=['POST'])
def modificar_text_nota():
    nom_usuari = request.form['nom_usuari']
    titol_nota = request.form['titol_nota']
    nou_text = request.form['nou_text']

    if BlocNotes.modificar_text_nota(nom_usuari, titol_nota, nou_text):
        return "Text modificat correctament"
    else:
        return "No hi ha cap nota amb aquest titol"


@CONNEXIO.route('/eliminar_nota', methods=['POST'])
def eliminar_nota():
    nom_usuari = request.form['nom_usuari']
    titol_nota = request.form['titol_nota']
    if BlocNotes.eliminar_nota(nom_usuari, titol_nota):
        return "Nota eliminada amb èxit"
    else:
        return "No hi ha cap nota amb aquest titol"


@CONNEXIO.route('/eliminar_totes_notes', methods=['POST'])
def eliminar_totes_notes():
    nom_usuari = request.form['nom_usuari']
    BlocNotes.eliminar_totes_notes(nom_usuari)
    return "Notes eliminades amb èxit"
