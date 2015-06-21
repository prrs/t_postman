import logging
from logging.handlers import TimedRotatingFileHandler

class log():
    def __init__(self):

        self.logger = logging.basicConfig(filename='application.log' ,level=logging.DEBUG, format='[%(levelname)s] %(asctime)s-15s [%(pathname)s %(lineno)d ] %(message)s',)
        handler = TimedRotatingFileHandler('aplication.log', when="midnight", interval=1)
        logging.getLogger('').addHandler(handler)
