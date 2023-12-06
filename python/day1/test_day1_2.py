import unittest

from day1_2 import string_to_numbers, numbers_to_answer


class TestSum(unittest.TestCase):

    def test_string_to_numbers(self):
        cases = {
            '9oneight8': '9188',
            '9eightone': '981',
            'hczsqfour3nxm5seven4': '43574',
            '9twopjqkghmbone': '921',
            'rhrfthv886vflthreeztvzs': '8863',
            'tlbtwo62five': '2625',
            'ninetwonine234nvtlzxzczx': '929234',
            '28sevenseven': '2877',
            '2sevensxszqdhjg2threexzjj3': '27233',
            '2fvq': '2',
            '781dk97eight26': '78197826',
            'plfrsjtbl6': '6',
            'sixglj13': '613',
            'b3seven6817gjpcxseven': '3768177',
            '3fivenlqcbszfoursixfive6sixfb': '3546566',
            'zfxbzhczcx9eightwockk': '982', #
            'threedssqrlk2qnpkzpkdddt': '32',
            'three67fourkbrlkf7mtbjprrth2': '367472',
            'seven3oneightp': '7318', #
            '31three': '313',
            '3894sevenfourfour': '3894744',
            '3ghmqlnine': '39',
            '7nine5zplh': '795',
            '3three9': '339',
            'ff6dhvzmdrgt': '6',
            '3one3four': '3134',
            'fourvptdnbpqcxktwoone4oneone': '421411',
            'd6': '6',
            '4kthx2': '42',
            'ktjvrmdjf27five8one': '27581',
            '94gkvkghfjqpsix': '946',
            '4gzstfpbqblqkxqrvd': '4',
            'eight1nine': '819',
            '8zgpptkqjdglpkssbpgzmn85': '885',
            'sixpmfjrdmcj76': '676',
            'six97': '697',
            'ninesxs7': '97',
            'one5lvxpfbnlfq': '15',
            'jninepzpgtzq7four5': '9745',
            'fourvjjrttlvdtfour8qxdvlg22two': '448222',
            '7pqqdrrvcmvbr8nine57': '78957'
        }

        for i, o in cases.items():
            self.assertEqual(string_to_numbers(i), o)

    def test_numbers_to_answer(self):
        self.assertEqual(numbers_to_answer('448222'), 42)
        self.assertEqual(numbers_to_answer('1'), 11)

