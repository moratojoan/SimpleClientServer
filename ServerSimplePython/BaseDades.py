from flask_sqlalchemy import SQLAlchemy
import datetime

bd = SQLAlchemy()


class Usuari(bd.Model):
    __tablename__ = 'usuaris'

    id = bd.Column(bd.Integer, primary_key=True)
    nom_usuari = bd.Column(bd.Text(), unique=True)
    email = bd.Column(bd.Text())
    contrasenya = bd.Column(bd.Text())
    text = bd.relationship('BlocNotes')

    def __init__(self, nom_usuari, email, contrasenya):
        self.nom_usuari = nom_usuari
        self.email = email
        self.contrasenya = contrasenya

    @classmethod
    def afegir_usuari(cls, nom_usuari, email, contrasenya):
        usuari = cls(nom_usuari, email, contrasenya)
        bd.session.add(usuari)
        bd.session.commit()

    @classmethod
    def obtenir_usuari(cls, nom_usuari):
        return bd.session.query(cls).filter_by(nom_usuari=nom_usuari).first()

    @classmethod
    def eliminar_usuari(cls, nom_usuari):
        usuari = cls.obtenir_usuari(nom_usuari)
        BlocNotes.eliminar_totes_notes(usuari.nom_usuari)
        bd.session.delete(usuari)
        bd.session.commit()


class BlocNotes(bd.Model):
    id = bd.Column(bd.Integer, primary_key=True)
    nom_usuari = bd.Column(bd.Text(), bd.ForeignKey('usuaris.nom_usuari'))
    titol_nota = bd.Column(bd.Text())
    text = bd.Column(bd.Text())
    data_creacio = bd.Column(bd.DateTime, default=datetime.datetime.now)

    def __init__(self, nom_usuari, titol_nota, text):
        self.nom_usuari = nom_usuari
        self.titol_nota = titol_nota
        self.text = text

    @classmethod
    def afegir_nota(cls, nom_usuari, titol_nota, text):
        nota = cls(nom_usuari, titol_nota, text)
        bd.session.add(nota)
        bd.session.commit()

    @classmethod
    def obtenir_nota(cls, nom_usuari, titol_nota):
        return bd.session.query(cls).filter_by(nom_usuari=nom_usuari).filter_by(titol_nota=titol_nota).first()

    @classmethod
    def obtenir_totes_notes(cls, nom_usuari):
        return bd.session.query(cls).filter_by(nom_usuari=nom_usuari)

    @classmethod
    def modificar_titol_nota(cls, nom_usuari, titol_nota, nou_titol_nota):
        nota = cls.obtenir_nota(nom_usuari, titol_nota)
        if nota is not None:
            nota.titol_nota = nou_titol_nota
            bd.session.commit()
            return True
        else:
            return False

    @classmethod
    def modificar_text_nota(cls, nom_usuari, titol_nota, nou_text):
        nota = cls.obtenir_nota(nom_usuari, titol_nota)
        if nota is not None:
            nota.text = nou_text
            bd.session.commit()
            return True
        else:
            return False

    @classmethod
    def eliminar_nota(cls, nom_usuari, titol_nota):
        nota = cls.obtenir_nota(nom_usuari, titol_nota)
        if nota is not None:
            bd.session.delete(nota)
            bd.session.commit()
            return True
        else:
            return False

    @classmethod
    def eliminar_totes_notes(cls, nom_usuari):
        llista_notes = cls.obtenir_totes_notes(nom_usuari)
        for i in llista_notes:
            cls.eliminar_nota(nom_usuari, i.titol_nota)
