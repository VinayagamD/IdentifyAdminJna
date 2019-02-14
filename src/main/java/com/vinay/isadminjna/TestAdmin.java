package com.vinay.isadminjna;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinNT;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestAdmin {
    private static Supplier<WinNT.PSIDByReference> psidByReferenceSupplier =  WinNT.PSIDByReference::new;
    private static Consumer<Advapi32Util.Account> accountConsumer = (s) -> System.out.println(s.sidString);
   private static Predicate<Advapi32Util.Account> isAdminGroup = (s)->{
       WinNT.PSIDByReference sid = psidByReferenceSupplier.get();
       Advapi32.INSTANCE.ConvertStringSidToSid(s.sidString,sid);
       return Advapi32.INSTANCE.IsWellKnownSid(sid.getValue(), WinNT.WELL_KNOWN_SID_TYPE.WinBuiltinAdministratorsSid);

   };
    public static void main(String[] args) {
        System.out.println("WinNt = "+WinNT.WELL_KNOWN_SID_TYPE.WinBuiltinAdministratorsSid);
        boolean isAdmin = Arrays.stream(Advapi32Util.getCurrentUserGroups()).anyMatch(isAdminGroup);
        System.out.println(isAdmin);
        Arrays.asList(Advapi32Util.getCurrentUserGroups()).forEach(accountConsumer);

    }
}
