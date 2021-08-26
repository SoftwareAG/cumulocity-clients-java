package c8y;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WlanSsidInformationTest {

    @Test
    void objectShouldBeEquals() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:45", null, null));
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:60", null, null));

        WlanSsidInformation other = new WlanSsidInformation();
        other.add(new SsidInformation("00:26:3e:0c:71:45", null, null));
        other.add(new SsidInformation("00:26:3e:0c:71:60", null, null));

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isTrue();
    }

    @Test
    void objectShouldNotBeEquals() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:45", null, null));
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:60", "ssid", null));

        WlanSsidInformation other = new WlanSsidInformation();
        other.add(new SsidInformation("00:26:3e:0c:71:45", null, null));
        other.add(new SsidInformation("00:26:3e:0c:71:60", null, null));

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isFalse();
    }

    @Test
    void objectShouldBeEqualsForEmpty() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        WlanSsidInformation other = new WlanSsidInformation();

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isTrue();
    }

    @Test
    void objectShouldNotBeEqualsForNull() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        WlanSsidInformation other = null;

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isFalse();
    }

    @Test
    void objectShouldNotBeEqualsForMoreSsidInformation() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        WlanSsidInformation other = new WlanSsidInformation();
        other.add(new SsidInformation("00:26:3e:0c:71:45", null, null));

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isFalse();
    }

    @Test
    void objectShouldNotBeEqualsForLessSsidInformation() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:45", null, null));
        WlanSsidInformation other = new WlanSsidInformation();

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isFalse();
    }

    @Test
    void objectShouldNotBeEqualsForDifferentTypes() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        String other = "string type";

        //when
        boolean equals = wlanSsidInformation.equals(other);

        //then
        assertThat(equals).isFalse();
    }

    @Test
    void objectShouldNotBeEqualsForTheSameObject() {
        //given
        WlanSsidInformation wlanSsidInformation = new WlanSsidInformation();
        wlanSsidInformation.add(new SsidInformation("00:26:3e:0c:71:45", null, null));

        //when
        boolean equals = wlanSsidInformation.equals(wlanSsidInformation);

        //then
        assertThat(equals).isTrue();
    }
}
