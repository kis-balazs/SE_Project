class SingletonException(Exception):
    """my specific exception created for the singleton class"""

    def __init__(self, msg):
        print(msg)
