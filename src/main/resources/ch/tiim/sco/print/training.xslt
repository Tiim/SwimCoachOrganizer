<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="2.0">


    <xsl:output method="xml" indent="yes"/>

    <xsl:key name="set" match="/SwimCoachOrganizer/Sets/Set" use="@id"/>
    <xsl:key name="focus" match="/SwimCoachOrganizer/Foci/Focus" use="@id"/>
    <xsl:key name="stroke" match="/SwimCoachOrganizer/Strokes/Stroke" use="@id"/>

    <!--

    Match root
    once:
    *Page Master
    *Page Sequence

    -->
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-width="210mm" page-height="297mm"
                                       margin-top="2cm" margin-bottom="2cm"
                                       margin-left="1.0cm" margin-right="1.0cm">
                    <fo:region-body margin="1cm"/>
                    <fo:region-before extent="1cm"/>
                    <fo:region-after extent="1cm"/>
                    <fo:region-start extent="1cm"/>
                    <fo:region-end extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4">
                <!-- Page content goes here -->
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="10pt" font-family="Helvetica">
                        <xsl:apply-templates select="SwimCoachOrganizer"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <!--

    Match SwimCoachOrganizer
    For each training:
    * Print Name
    * Build Table
    * Fill Table with Sets
    * Print Total distance

    -->

    <xsl:template match="SwimCoachOrganizer">
        <xsl:for-each select="Trainings/Training">
            <fo:block font-size="16pt" font-weight="bold" margin-top="10mm" margin-bottom="5mm">
                <xsl:value-of select="Date"/>
            </fo:block>
            <fo:block border-bottom-width="0.5pt"
                      border-bottom-style="solid"
                      border-bottom-color="black"
                      border-top-width="0.5pt"
                      border-top-style="solid"
                      border-top-color="black">
                <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="10%"/>
                    <fo:table-column column-width="68%"/>
                    <fo:table-column column-width="10%"/>
                    <fo:table-column column-width="6%"/>
                    <fo:table-column column-width="6%"/>

                    <fo:table-body>
                        <xsl:apply-templates select="SetsID"/>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <fo:block font-weight="bold" margin-top="2pt">
                <xsl:value-of select="concat(Distance,'m')"/>
            </fo:block>
        </xsl:for-each>
    </xsl:template>

    <!--

    Match Sets
    For each set:
    * Fill table with relevant data

    -->

    <xsl:template match="SetsID">
        <xsl:for-each select="SetID">
            <xsl:sort select="@index"/>
            <fo:table-row>
                <fo:table-cell>
                    <fo:block margin-top="1pt" margin-bottom="1pt">
                        <xsl:if test="key('set',current())/Distance1!=1">
                            <xsl:value-of select="concat(key('set',current())/Distance1,'x')"/>
                        </xsl:if>
                        <xsl:if test="key('set',current())/Distance2!=1">
                            <xsl:value-of select="concat(key('set',current())/Distance2,'x')"/>
                        </xsl:if>
                        <xsl:value-of select="concat(key('set',current())/Distance3,'m')"/>
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block margin-top="1pt">
                        <xsl:value-of select="key('set',current())/Name"/>
                    </fo:block>
                    <fo:block margin-bottom="1pt">
                        <xsl:value-of select="key('set',current())/Content"/>
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block margin-top="1pt" margin-bottom="1pt">
                        <xsl:variable name="ms" select="key('set',current())/Interval"/>
                        <xsl:variable name="min" select="floor($ms div (1000*60)) mod 60"/>
                        <xsl:variable name="sec" select="floor($ms div 1000) mod 60"/>
                        <xsl:variable name="hs" select="floor(($ms mod 1000) div 100)"/>
                        <xsl:variable name="time">
                            <xsl:if test="$min>0">
                                <xsl:value-of select="concat($min,':')"/>
                            </xsl:if>
                            <xsl:value-of select="format-number($sec,'00')"/>
                            <xsl:if test="$hs>0">
                                <xsl:value-of select="concat('.',format-number($hs,'00'))"/>
                            </xsl:if>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when test="key('set',current())/Interval=0">-</xsl:when>
                            <xsl:when test="key('set', current())/IsPause = 'true'">
                                <xsl:value-of select="concat('P',$time)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="concat('@',$time)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block margin-top="1pt" margin-bottom="1pt">
                        <xsl:value-of select="key('stroke', key('set',current())/StrokeID)/Abbr"/>
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block margin-top="1pt" margin-bottom="1pt">
                        <xsl:value-of select="key('focus', key('set',current())/FocusID)/Abbr"/>
                    </fo:block>
                </fo:table-cell>
            </fo:table-row>
        </xsl:for-each>
    </xsl:template>


</xsl:stylesheet>