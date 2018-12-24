from flask import Flask
from BaseDades import bd
import Connexio
import os

DEBUG = True
DB_PATH = os.path.realpath(os.path.join(os.path.dirname(__file__), "servidor-simple.db"))


def crear_app():
    app = Flask(__name__)
    app.secret_key = 'dev'
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///%s' % DB_PATH
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    bd.init_app(app)
    if not os.path.exists(DB_PATH):
        bd.create_all(app=app)
    app.register_blueprint(Connexio.CONNEXIO)

    return app


def main():
    crear_app().run(debug=DEBUG)


if __name__ == '__main__':
    main()
