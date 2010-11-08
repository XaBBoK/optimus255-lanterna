/*
 *  Copyright (C) 2010 mabe02
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lantern.test;

/**
 *
 * @author mabe02
 */
public class TestAllCodes {
    public static void main(String[] args) throws Exception
    {
        System.out.write(new byte[] { (byte)0x1B, 0x28, 0x30 });
        for(int i = 0; i < 200; i++) {
            System.out.write((i + " = " + ((char)i) + "\n").getBytes());
        }
        System.out.write(new byte[] { (byte)0x1B, 0x28, 0x42 });
        //System.out.write(new byte[] { (byte)0x1B, (byte)0x21, (byte)0x40, 15 });
    }
}
