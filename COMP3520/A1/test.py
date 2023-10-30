import unittest

from pytheons.pyunit.decorators.test import test


class TestBasicOutputs(unittest.TestCase):
    @test
    def test_basic1(self):
        self.assertTrue(True)
