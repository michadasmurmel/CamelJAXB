<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output standalone="yes" indent="yes" />
	<xsl:template match="/">
		<priceList>
			<!-- <xsl:for-each select="ProductList/Product">
				<product>
					<xsl:attribute name="name">
						<xsl:value-of select="@name" /> 
					</xsl:attribute>
				</product>
			</xsl:for-each> -->

			<xsl:for-each
				select="ProductList/Product/manufacturer[generate-id()
                                       = generate-id(.)]">
				<vendor>
					<xsl:attribute name="name">
						<xsl:value-of select="@name" /> 
					</xsl:attribute>
				</vendor>
			</xsl:for-each>
		</priceList>
	</xsl:template>
</xsl:stylesheet> 