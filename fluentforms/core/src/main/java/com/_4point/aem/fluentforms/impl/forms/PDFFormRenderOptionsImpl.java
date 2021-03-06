package com._4point.aem.fluentforms.impl.forms;


import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import com._4point.aem.fluentforms.api.AbsoluteOrRelativeUrl;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.PathOrUrl;
import com._4point.aem.fluentforms.api.forms.PDFFormRenderOptions;
import com._4point.aem.fluentforms.api.forms.PDFFormRenderOptionsSetter;
import com.adobe.fd.forms.api.AcrobatVersion;
import com.adobe.fd.forms.api.CacheStrategy;
import com.adobe.fd.forms.api.RenderAtClient;

public class PDFFormRenderOptionsImpl implements PDFFormRenderOptionsSetter, PDFFormRenderOptions {

	private AcrobatVersion acrobatVersion = null;
	private CacheStrategy cacheStrategy = null;
	private PathOrUrl contentRoot = null;
	private Path debugDir = null;
	private Boolean embedFonts = null;
	private Locale locale = null;
	private RenderAtClient renderAtClient = null;
	private List<AbsoluteOrRelativeUrl> submitUrls = null;
	private Boolean taggedPDF = null;
	private Document xci = null;

	@Override
	public AcrobatVersion getAcrobatVersion() {
		return acrobatVersion;
	}

	@Override
	public PDFFormRenderOptionsImpl setAcrobatVersion(AcrobatVersion acrobatVersion) {
		this.acrobatVersion = acrobatVersion;
		return this;
	}

	@Override
	public CacheStrategy getCacheStrategy() {
		return cacheStrategy;
	}

	@Override
	public PDFFormRenderOptionsImpl setCacheStrategy(CacheStrategy cacheStrategy) {
		this.cacheStrategy = cacheStrategy;
		return this;
	}

	@Override
	public PathOrUrl getContentRoot() {
		return contentRoot;
	}

	@Override
	public PDFFormRenderOptionsImpl setContentRoot(PathOrUrl contentRootPathOrUrl) {
		this.contentRoot = contentRootPathOrUrl;
		return this;
	}

	@Override
	public Path getDebugDir() {
		return debugDir;
	}

	@Override
	public PDFFormRenderOptionsImpl setDebugDir(Path debugDir) {
		this.debugDir = debugDir;
		return this;
	}

	@Override
	public Boolean getEmbedFonts() {
		return embedFonts;
	}

	@Override
	public PDFFormRenderOptionsImpl setEmbedFonts(boolean embedFonts) {
		this.embedFonts = embedFonts;
		return this;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public RenderAtClient getRenderAtClient() {
		return this.renderAtClient;
	}

	@Override
	public PDFFormRenderOptionsImpl setRenderAtClient(RenderAtClient renderAtClient) {
		this.renderAtClient = renderAtClient;
		return this;
	}

	@Override
	public PDFFormRenderOptionsImpl setLocale(Locale locale) {
		this.locale = locale;
		return this;
	}

	@Override
	public List<AbsoluteOrRelativeUrl> getSubmitUrls() {
		return submitUrls;
	}

	@Override
	public PDFFormRenderOptionsImpl setSubmitUrls(List<AbsoluteOrRelativeUrl> submitUrls) {
		this.submitUrls = submitUrls;
		return this;
	}

	@Override
	public boolean isTaggedPDF() {
		return taggedPDF;
	}

	@Override
	public Boolean getTaggedPDF() {
		return taggedPDF;
	}

	@Override
	public PDFFormRenderOptionsImpl setTaggedPDF(boolean taggedPDF) {
		this.taggedPDF = taggedPDF;
		return this;
	}

	@Override
	public Document getXci() {
		return xci;
	}

	@Override
	public PDFFormRenderOptionsImpl setXci(Document xci) {
		this.xci = xci;
		return this;
	}
}
